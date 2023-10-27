package com.GalTides.entities;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TideDTO {

    private String name;
    private List<Tide> tides;
}
