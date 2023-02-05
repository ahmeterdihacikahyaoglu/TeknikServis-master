package com.garanti.TeknikServis.controller;

import com.garanti.TeknikServis.model.Users;
import com.garanti.TeknikServis.service.UserService;
import com.garanti.TeknikServis.validation.UserValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@AllArgsConstructor
@Tag (name = "User Service", description = "This class enables to user operations.")
@SecurityScheme (
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class UserController
{
    private UserService service;
    private MessageSource messageSource;

    private UserValidator userValidator;


    @Operation (summary = "This method is to fetch the users from database.")
    @SecurityRequirement (name = "Bearer Authentication")
    @GetMapping(path = "test")
    @Secured(value = "ROLE_ADMIN")
    public String getByUserName()
    {
        //localhost:9090/test
        return "merhaba";

    }


    @PostMapping(path = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity save(@Validated @RequestBody Users users, BindingResult result, @RequestHeader (name = "Accept-Language", required = false) Locale locale)
    {
        //localhost:9090/save
        // {"username":"test","password":"1234","user_EMAIL":"test@gmail.com"}

        userValidator.validate(users, result);

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        if (service.userSave(users.getUSERNAME(),users.getPASSWORD(), users.getUSER_EMAIL()))
        {
            return ResponseEntity.status(HttpStatus.CREATED).body(messageSource.getMessage("user.save.success", null,locale));
        }
        else
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageSource.getMessage("user.save.fail", null, locale));
        }
    }

}
