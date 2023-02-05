package com.garanti.TeknikServis.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Product
{
    private Integer PRODUCT_ID;
    @NonNull
    private String NAME;

}
