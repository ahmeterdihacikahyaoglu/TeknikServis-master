package com.garanti.TeknikServis.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sale
{
    //bu sınıf parametresiz constructor içerir.
    //bu sınıf bütün parametreleri içeren constructor içerir.
    //bu sınıf, bu sınıfa ait olan PK değeri "SALE_ID" hariç diğer bütün değişkenleri içeren constructor içerir.
    private Integer SALE_ID;
    @NonNull
    private Integer PRICE;
    @NonNull
    private String NOTE;
    @NonNull
    private Integer PRODUCT_ID;
    @NonNull
    private boolean IS_SOLD;

}
