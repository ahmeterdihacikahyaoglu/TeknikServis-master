package com.garanti.TeknikServis.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestErrorResponse {

    private String errorDate;
    private String errorMessage;
    private String detail;
}