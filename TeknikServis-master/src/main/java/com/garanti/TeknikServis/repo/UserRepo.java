package com.garanti.TeknikServis.repo;

import com.garanti.TeknikServis.model.Users;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class UserRepo
{
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate jdbcTemplate;

    public Users getByUserName(String username)
    {
        String sql = "select * from GARANTI.USERS where USERNAME = :USERNAME";
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("USERNAME", username);
        return  namedParameterJdbcTemplate.queryForObject(sql, paramMap, BeanPropertyRowMapper.newInstance(Users.class));
    }

    public List<GrantedAuthority> getUserRoles(Integer id)
    {
        String sql = "select ROLE from GARANTI.USER_ROLES where USER_ID = :USER_ID";
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("USER_ID", id);
        List<String> liste = namedParameterJdbcTemplate.queryForList(sql, paramMap, String.class);
        List<GrantedAuthority> roles = new ArrayList<>();
        for (String role : liste)
        {
            roles.add(new SimpleGrantedAuthority(role));
        }
        return roles;
    }

    public int getUserId(String username){
        String sql = "SELECT USER_ID FROM USERS WHERE USERNAME = :USERNAME";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("USERNAME", username);
        return namedParameterJdbcTemplate.queryForObject(sql,paramMap,Integer.class);
    }

    @Transactional
    public boolean save(Users customUser)
    {
        String sql = "Insert into GARANTI.USERS (USER_ID,USERNAME, PASSWORD, USER_EMAIL) values (:USER_ID, :USERNAME, :PASSWORD,:USER_EMAIL)";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("USER_ID",customUser.getUSER_ID());
        paramMap.put("USERNAME", customUser.getUSERNAME());
        paramMap.put("PASSWORD", customUser.getPASSWORD());
        paramMap.put("USER_EMAIL", customUser.getUSER_EMAIL());
        namedParameterJdbcTemplate.update(sql, paramMap);
        sql = "Insert into GARANTI.USER_ROLES (USER_ID, ROLE) values (:USER_ID, :ROLE)";
        paramMap = new HashMap<>();
        paramMap.put("USER_ID", customUser.getUSER_ID());
        paramMap.put("ROLE", "ROLE_USER");
        return namedParameterJdbcTemplate.update(sql, paramMap) == 1;
    }

    public Integer getNextId()
    {
        String sql = "SELECT MAX(USER_ID)+1 FROM GARANTI.USERS";
        return  jdbcTemplate.queryForObject(sql, Integer.class);
    }
}