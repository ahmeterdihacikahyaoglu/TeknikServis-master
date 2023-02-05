package com.garanti.TeknikServis.repo;

import com.garanti.TeknikServis.model.Sale;
import com.garanti.TeknikServis.model.SaleDto;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class SaleRepository {

    private JdbcTemplate jdbcTemplate;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List< Sale > getAll () {
        String sql = "select * from GARANTI.SALE ORDER BY SALE_ID DESC";

        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Sale.class));
    }

    public boolean deleteById (Integer id) {
        String sql = "delete from GARANTI.SALE where SALE_ID = :ID";
        Map< String, Object > parameter = new HashMap<>();
        parameter.put("ID", id);

        return namedParameterJdbcTemplate.update(sql, parameter) == 1;
    }

    public boolean save (Sale sale) {
        String sql = "Insert into GARANTI.SALE (PRODUCT_ID,NOTE,PRICE, IS_SOLD) values (:PRODUCT, :NOTE, :PRICE, :SOLD)";
        Map< String, Object > paramMap = new HashMap<>();
        paramMap.put("PRODUCT", sale.getPRODUCT_ID());
        paramMap.put("NOTE", sale.getNOTE());
        paramMap.put("PRICE", sale.getPRICE());
        paramMap.put("SOLD", sale.isIS_SOLD());

        return namedParameterJdbcTemplate.update(sql, paramMap) == 1;
    }

    // user metotları:
    public List <SaleDto> getListofSales(){
        String sql = "SELECT S.SALE_ID AS SALE_ID, S.PRICE AS PRICE, S.NOTE AS NOTE, P.NAME AS PRODUCT FROM SALE S\n" +
                "INNER JOIN PRODUCT P ON P.PRODUCT_ID = S.PRODUCT_ID WHERE S.IS_SOLD = 0";
        RowMapper<SaleDto> rowMapper = new RowMapper<SaleDto>() {
            @Override
            public SaleDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new SaleDto(rs.getInt("SALE_ID"), (rs.getDouble("PRICE")+" TL"), rs.getString("NOTE"), rs.getString("PRODUCT"));
            }
        };
        return jdbcTemplate.query(sql,rowMapper);
    }
    public List <SaleDto> getListofSalesByProduct(String productType){
        String sql = "SELECT S.SALE_ID AS SALE_ID, S.PRICE AS PRICE, S.NOTE AS NOTE, P.NAME AS PRODUCT FROM SALE S\n" +
                "INNER JOIN PRODUCT P ON P.PRODUCT_ID = S.PRODUCT_ID WHERE lower(P.NAME) LIKE :PRODUCT_NAME AND S.IS_SOLD = 0";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("PRODUCT_NAME", "%"+productType.toLowerCase()+"%");
        RowMapper<SaleDto> rowMapper = new RowMapper<SaleDto>() {
            @Override
            public SaleDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new SaleDto(rs.getInt("SALE_ID"), (rs.getDouble("PRICE")+" TL"), rs.getString("NOTE"), rs.getString("PRODUCT"));
            }
        };
        return namedParameterJdbcTemplate.query(sql, paramMap, rowMapper);
    }

    public List <SaleDto> getListofSalesByProductId(Integer productId){
        String sql = "SELECT S.SALE_ID AS SALE_ID, S.PRICE AS PRICE, S.NOTE AS NOTE, P.NAME AS PRODUCT FROM SALE S\n" +
                "INNER JOIN PRODUCT P ON P.PRODUCT_ID = S.PRODUCT_ID WHERE P.PRODUCT_ID = :PRODUCT_ID AND S.IS_SOLD = 0";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("PRODUCT_ID", productId);
        RowMapper<SaleDto> rowMapper = new RowMapper<SaleDto>() {
            @Override
            public SaleDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new SaleDto(rs.getInt("SALE_ID"), (rs.getDouble("PRICE")+" TL"), rs.getString("NOTE"), rs.getString("PRODUCT"));
            }
        };
        return namedParameterJdbcTemplate.query(sql, paramMap, rowMapper);
    }
    public boolean buyTheProductInAd(Integer id){
        String sql = "UPDATE SALE SET IS_SOLD = 1 WHERE SALE_ID = :SALE_ID AND IS_SOLD != 1";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("SALE_ID", id);
        return namedParameterJdbcTemplate.update(sql,paramMap) == 1;
    }
    public boolean insertSaleToSaleLog(Integer id, Integer userid, String creditcard){
        String sql = "INSERT INTO  SALE_LOG(USER_ID, SALE_ID, INFO, SALE_LOG_DATE, CREDIT_CARD) VALUES" +
                "(:USER_ID, :SALE_ID, :INFO, :SALE_LOG_DATE, :CREDIT_CARD)";
        Sale sale = getSaleById(id);
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("USER_ID", userid);
        paramMap.put("SALE_ID", sale.getSALE_ID());
        paramMap.put("INFO", sale.getNOTE());
        paramMap.put("SALE_LOG_DATE", LocalDateTime.now());
        paramMap.put("CREDIT_CARD", creditcard);
        return namedParameterJdbcTemplate.update(sql,paramMap) == 1;
    }
    private Sale getSaleById(Integer id){
        String sql = "SELECT * FROM SALE WHERE SALE_ID = :ID";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("ID", id);
        return namedParameterJdbcTemplate.queryForObject(sql,paramMap,BeanPropertyRowMapper.newInstance(Sale.class));
    }
}
