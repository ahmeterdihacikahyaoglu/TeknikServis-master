package com.garanti.TeknikServis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProposalDto {
    private Integer id;
    private Integer price;
    private String note;
    private String product;
    private String username;
    private String approval;
    private String date;
}
