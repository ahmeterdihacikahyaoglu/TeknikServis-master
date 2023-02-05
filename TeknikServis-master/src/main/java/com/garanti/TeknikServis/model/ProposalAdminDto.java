package com.garanti.TeknikServis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProposalAdminDto {

    private String NAME;

    private long PRICE;

    private  String NOTE;

}
