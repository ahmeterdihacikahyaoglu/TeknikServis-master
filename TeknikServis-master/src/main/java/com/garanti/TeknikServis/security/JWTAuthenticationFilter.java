package com.garanti.TeknikServis.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garanti.TeknikServis.model.Users;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager)
    {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException
    {
        try
        {
            Users creds = new ObjectMapper().readValue(req.getInputStream(), Users.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(creds.getUSERNAME(), creds.getPASSWORD(), new ArrayList<GrantedAuthority>()));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException
    {

        User principal = ((User) auth.getPrincipal());
        String rolestring = principal.getAuthorities().toArray()[0].toString();
        String str = principal.getUsername() + "-" + rolestring;
        String token = JWT.create().withSubject(str).withExpiresAt(new Date(System.currentTimeMillis() + 9000000)).sign(Algorithm.HMAC512("MY_SECRET_KEY".getBytes()));
        String body = "(" + principal.getUsername() + ") " + token;
        res.getWriter().write(body);
        res.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException
    {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        if (failed.getClass() == DisabledException.class)
        {
            response.getWriter().write("Kullanıcı disabled olmuş");
        }
        else
        {
            response.getWriter().write("Kullanıcı adı şifre yanlış");
        }
        response.getWriter().flush();
    }
}