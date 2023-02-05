package com.garanti.TeknikServis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRoles
{
    //bu sınıf parametresiz constructor içerir.
    //bu sınıf bütün parametreleri içeren constructor içerir.

    private String ROLE;
    private Integer USER_ID;
}
