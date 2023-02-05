package com.garanti.TeknikServis.excepton;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
public class MultipleProposalCreationException extends RuntimeException{

    public MultipleProposalCreationException(String message)
    {
        super(message);
    }

}
