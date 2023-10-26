package com.GalTides.entities;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TideDetail {
    private String estado;
    private String hora;
    private String altura;
}
