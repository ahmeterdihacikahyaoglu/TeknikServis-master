package com.garanti.TeknikServis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@AllArgsConstructor
public class Role implements GrantedAuthority
{

    private static final long serialVersionUID = 9156064156119386503L;
    private String NAME;

    @Override
    public String getAuthority() {
        return NAME;
    }
}
