package com.garanti.TeknikServis.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class Users
{
    private Integer USER_ID;
    private String USERNAME ;
    private String PASSWORD ;
    private String USER_EMAIL ;

    public Users(Integer USER_ID, String USERNAME, String PASSWORD, String USER_EMAIL) {
        this.USER_ID = USER_ID;
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
        this.USER_EMAIL = USER_EMAIL;
    }
}
