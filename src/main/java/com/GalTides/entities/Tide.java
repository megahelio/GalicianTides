package com.GalTides.entities;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Tide {
    private String name;
    private String date;
    private List<TideDetail> tideDetail;
}
