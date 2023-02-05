package com.garanti.TeknikServis.validation;

import com.garanti.TeknikServis.model.Users;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Users.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Users user = (Users) target;

        // Validation  for username
        if (user.getUSERNAME().isEmpty() ) {
            errors.rejectValue("USERNAME",  "Kullanıcı adı boş bırakılamaz...");
        }

        //Validation for password
        if (user.getPASSWORD().isEmpty()) {
            errors.rejectValue("PASSWORD",  "Lütfen şifrenizi giriniz.");
            return;
        }
        if (user.getPASSWORD().length() <5) {
            errors.rejectValue("PASSWORD",  "Şifreniz en az 5 karakter uzunluğunda olmalıdır.");
        }

        //Validation logic for email
        if (user.getUSER_EMAIL().isEmpty()) {
            errors.rejectValue("USER_EMAIL",  "Lütfen mail adresinizi giriniz.");
            return;
        }

        if (!user.getUSER_EMAIL().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            errors.rejectValue("USER_EMAIL",  "Lütfen mail formatında giriniz.");
        }

    }
}