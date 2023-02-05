package com.garanti.TeknikServis.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class Service
{
    private Integer SERVICE_ID;
    private String SERVICE_NAME;
    private Integer DESKTOP;
    private Integer LAPTOP;
    private Integer MAC;
    private Integer DURATION;
}
