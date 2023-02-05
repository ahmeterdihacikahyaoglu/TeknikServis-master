package com.garanti.TeknikServis.model;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class SaleLog
{
    //bu sınıf parametresiz constructor içerir.
    //bu sınıf bütün parametreleri içeren constructor içerir.
    //bu sınıf, bu sınıfa ait olan PK değeri "SALE_LOG_ID" hariç diğer bütün değişkenleri içeren constructor içerir.
    private Integer SALE_LOG_ID;
    @NonNull
    private Integer USER_ID;
    @NonNull
    private Integer SALE_ID;
    @NonNull
    private String INFO;
    @NonNull
    private LocalDateTime SALE_LOG_DATE;
    @NonNull
    private String CREDIT_CARD;

}
