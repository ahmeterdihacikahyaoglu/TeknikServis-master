package com.garanti.TeknikServis.model;

import lombok.*;

import java.sql.Date;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Booking
{
    //bu sınıf parametresiz constructor içerir.
    //bu sınıf bütün parametreleri içeren constructor içerir.
    //bu sınıf, bu sınıfa ait olan PK değeri "B_ID" hariç diğer bütün değişkenleri içeren constructor içerir.
    private Integer B_ID;
    @NonNull
    private String NOTE;
    @NonNull
    private Integer USER_ID;
    @NonNull
    private Integer SERVICE_ID;
    private boolean IS_DONE;
    private Date BOOKING_DATE;

}
