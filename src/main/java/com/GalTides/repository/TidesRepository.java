package com.GalTides.repository;


import com.GalTides.entities.Tide;
import com.GalTides.entities.TideDTO;
import com.GalTides.entities.TideDetail;
import org.springframework.stereotype.Repository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class TidesRepository {

    /**
     * Busca objetos marea en https://servizos.meteogalicia.gal/mgrss/predicion/rssMareas.action
     *
     * @param parameters range: dataIni = dd/MM/aaaa & dataFin = dd/MM/aaaa & idPorto = num
     *                   single date:  data= dd/MM/aaaa & idPorto = num
     * @return TideDTO
     */
    public TideDTO getTides(Map<String, String> parameters) {
        String tidesrss = "";

        if (parameters.containsKey("dataIni") && parameters.containsKey("dataFin") && datesWrong(parameters.get("dataIni"), parameters.get("dataFin"))) {
            throw new RuntimeException();
        }

        try {
            URL url = new URL("https://servizos.meteogalicia.gal/mgrss/predicion/rssMareas.action");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            try (DataOutputStream out = new DataOutputStream(con.getOutputStream())) {
                out.writeBytes(ParameterStringBuilder.getParamsString(parameters));//include parameters
                out.flush();
            }
            System.out.println(ParameterStringBuilder.getParamsString(parameters));
            ;

            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine).append("\n");
            }
            tidesrss = content.toString();
            in.close();
            con.disconnect();

        } catch (
                Exception e) {
            e.printStackTrace();
        }
        System.out.println("tiderss: \n" + tidesrss);
        return parseRSS(tidesrss);
    }

    private boolean datesWrong(String dataIni, String dataFin) {
        try {
            SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
            return date.parse(dataIni).after(date.parse(dataFin));
        } catch (ParseException e) {
            throw new RuntimeException();
        }
    }

    private static TideDTO parseRSS(String rssContent) {
        TideDTO tideDTO = new TideDTO();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new StringReader(rssContent)));
            doc.getDocumentElement().normalize();

            NodeList itemNodes = doc.getElementsByTagName("item");
            List<Tide> tides = new ArrayList<>();


            for (int i = 0; i < itemNodes.getLength(); i++) {
                Element tideElement = (Element) itemNodes.item(i);
                System.out.println("tideElement: ");
                System.out.println(tideElement.getElementsByTagName("Mareas:nomePorto").item(0).getTextContent());
                System.out.println(tideElement.getElementsByTagName("Mareas:dataPredicion").item(0).getTextContent());

                if (i == 0) {
                    tideDTO.setName(tideElement.getElementsByTagName("Mareas:nomePorto").item(0).getTextContent());
                }
                Tide tide = Tide.builder()
                        .date(tideElement.getElementsByTagName("Mareas:dataPredicion").item(0).getTextContent())
                        .tideDetail(new ArrayList<>())
                        .build();

                NodeList tideDetailsNodeList = tideElement.getElementsByTagName("Mareas:mareas");
                for (int j = 0; j < tideDetailsNodeList.getLength(); j++) {
                    Element tideDetailElement = (Element) tideDetailsNodeList.item(j);

                    System.out.println(tideDetailElement.getAttribute("estado"));
                    System.out.println(tideDetailElement.getAttribute("hora"));
                    System.out.println(tideDetailElement.getAttribute("altura"));
                    TideDetail tideDetail = TideDetail.builder()
                            .estado(tideDetailElement.getAttribute("estado"))
                            .hora(tideDetailElement.getAttribute("hora"))
                            .altura(tideDetailElement.getAttribute("altura"))
                            .build();

                    tide.getTideDetail().add(tideDetail);
                }
                tides.add(tide);
            }
            tideDTO.setTides(tides);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tideDTO;
    }
}

