package com.garanti.TeknikServis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Integer B_ID;
    private String USERNAME;
    private String SERVICE_NAME;
    private Integer DURATION;
    private Integer DESKTOP;
    private Integer LAPTOP;
    private Integer MAC;
    private LocalDate BOOKING_DATE;
    private String NOTE;
    //getleme işlemi için

}
