package com.garanti.TeknikServis.validation.validConfig;

import com.garanti.TeknikServis.validation.BookingValidator;
import com.garanti.TeknikServis.validation.ProposalValidator;
import com.garanti.TeknikServis.validation.UserValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class validConfig {



    @Bean
    public UserValidator userValidator() {
        return new UserValidator();
    }
    @Bean
    public BookingValidator bookingValidator()
    {
        return new BookingValidator();
    }
    @Bean
    public ProposalValidator proposalValidator()
    {
        return new ProposalValidator();
    }


}