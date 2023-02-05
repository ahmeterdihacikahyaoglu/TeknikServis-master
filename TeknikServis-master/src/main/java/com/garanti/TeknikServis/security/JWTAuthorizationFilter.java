package com.garanti.TeknikServis.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JWTAuthorizationFilter extends OncePerRequestFilter
{
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException
    {
        String token = req.getHeader("Authorization");

        if (token != null)
        {
            try
            {
                String user = JWT.require(Algorithm.HMAC512("MY_SECRET_KEY".getBytes())).build().verify(token.replace("Bearer ", "")).getSubject();
                System.err.println("------> kullanıcı adı ve rol = " + user);
                if (user != null)
                {
                    String username = user.split("-")[0];
                    SimpleGrantedAuthority auth = new org.springframework.security.core.authority.SimpleGrantedAuthority(user.split("-")[1]);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, Collections.singletonList(auth));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    chain.doFilter(req, res);
                }
            }
            catch (Exception e)
            {
                System.err.println(e.getMessage());
                res.setStatus(HttpStatus.UNAUTHORIZED.value());
                res.getWriter().write("Token exception => " + e.getMessage());
            }
        }
        else
        {
            chain.doFilter(req, res);
        }
    }
}