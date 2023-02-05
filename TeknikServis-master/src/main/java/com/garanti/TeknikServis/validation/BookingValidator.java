package com.garanti.TeknikServis.validation;

import com.garanti.TeknikServis.model.Booking;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class BookingValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Booking.class.equals(clazz);
    }
    @Override
    public void validate(Object target, Errors errors) {
        Booking booking = (Booking) target;

        // Validation logic for NOTE
        if(booking.getNOTE().isEmpty())
        {
            errors.rejectValue("NOTE","Lütfen randevu notu giriniz:");
        }
        if (booking.getNOTE().length() < 10) {
            errors.rejectValue("NOTE", "Randevu notu 10 karakterden daha az olamaz");
        }
        if (booking.getNOTE().length() > 300) {
            errors.rejectValue("NOTE", "Randevu notu 300 karakterden daha fazla olamaz");
        }

    }
}