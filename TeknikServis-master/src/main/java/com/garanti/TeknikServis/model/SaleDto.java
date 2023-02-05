package com.garanti.TeknikServis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDto {
    @Id
    private int id;
    private String price;
    private String note;
    private String product;
}
