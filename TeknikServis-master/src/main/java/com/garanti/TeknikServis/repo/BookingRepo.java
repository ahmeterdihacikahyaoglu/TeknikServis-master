package com.garanti.TeknikServis.repo;

import com.garanti.TeknikServis.model.Booking;
import com.garanti.TeknikServis.model.BookingDTO;
import com.garanti.TeknikServis.model.Service;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class BookingRepo {


    private JdbcTemplate jdbcTemplate;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Booking getBookingById(int id)
    {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("B_ID", id);
        return namedParameterJdbcTemplate.queryForObject("select * from GARANTI.BOOKING where B_ID = :B_ID", paramMap, BeanPropertyRowMapper.newInstance(Booking.class));
    }

    public BookingDTO getById(int id)
    {
        BookingDTO bookingDTO = null;
        String sql = "SELECT b.B_ID,u.USERNAME,s.SERVICE_NAME,s.DURATION,s.DESKTOP,s.LAPTOP,s.MAC,b.NOTE,b.BOOKING_DATE from BOOKING b "
                +"INNER JOIN USERS u ON u.USER_ID=b.USER_ID "
                +"INNER JOIN SERVICE s ON s.SERVICE_ID=b.SERVICE_ID where b.B_ID = :ABUZIDDIN ";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("ABUZIDDIN", id);
        bookingDTO = namedParameterJdbcTemplate.queryForObject(sql, paramMap, BeanPropertyRowMapper.newInstance(BookingDTO.class));
        return bookingDTO;
    }



    public boolean deleteById(int id)
    {
        String sql = "DELETE FROM BOOKING where B_ID = :B_ID";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("B_ID", id);
        return namedParameterJdbcTemplate.update(sql, paramMap) == 1;
    }

    public boolean save (Booking booking,Date date)
    {
        String sql = "INSERT INTO BOOKING (NOTE,USER_ID,SERVICE_ID,BOOKING_DATE, IS_DONE) values (:NOTE,:USER_ID,:SERVICE_ID,:BOOKING_DATE, :IS_DONE)";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("NOTE", booking.getNOTE());
        paramMap.put("USER_ID", booking.getUSER_ID());
        paramMap.put("IS_DONE", '0');
        paramMap.put("SERVICE_ID", booking.getSERVICE_ID());
        paramMap.put("BOOKING_DATE", date);
        return  namedParameterJdbcTemplate.update(sql,paramMap)==1;

    }
    public Integer getNextId()
    {
        //aynı anda çağrılırsa yanlış ıd döndürülebilir.
        String sql="SELECT MAX(B_ID) FROM BOOKING";
        return jdbcTemplate.queryForObject(sql,Integer.class);
    }

    public Service service_hour_control(Booking booking) {
        Service service= null;
        String sql = "SELECT DURATION FROM SERVICE WHERE SERVICE_ID =:A";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("A", booking.getSERVICE_ID());
        service=namedParameterJdbcTemplate.queryForObject(sql, paramMap, BeanPropertyRowMapper.newInstance(Service.class));
         //x=namedParameterJdbcTemplate.update(sql, paramMap);
        return service;
    }


    public List<Service> getDurationList(Date new_date) {
        Service services=null;
        String sql = "SELECT s.DURATION FROM SERVICE s "
        +"INNER JOIN BOOKING b ON b.SERVICE_ID=s.SERVICE_ID WHERE b.BOOKING_DATE=:NEW_DATE";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("NEW_DATE",new_date);
        return namedParameterJdbcTemplate.query(sql, paramMap, BeanPropertyRowMapper.newInstance(Service.class));
    }

    public List<Booking> getAppointmentDatesInOrder(String sortType) {
        String sql = "SELECT * FROM BOOKING ORDER BY BOOKING_DATE " + sortType;
        RowMapper<Booking> rowMapper = new RowMapper<Booking>() {
            @Override
            public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Booking(rs.getInt("B_ID"), rs.getString("NOTE"), rs.getInt("USER_ID"), rs.getInt("SERVICE_ID"), rs.getBoolean("IS_DONE"), rs.getDate("BOOKING_DATE"));
            }
        };
        return namedParameterJdbcTemplate.query(sql, rowMapper);
    }

    public List<Booking> getAllAppointmentLikeUsername(String username) {
        String sql = "SELECT B.B_ID, B.NOTE, B.USER_ID, B.SERVICE_ID, B.IS_DONE, B.BOOKING_DATE FROM BOOKING B \n" +
                "INNER JOIN USERS U ON U.USER_ID = B.USER_ID WHERE U.USERNAME LIKE :USERNAME AND IS_DONE = 1";
        RowMapper<Booking> rowMapper = new RowMapper<Booking>() {
            @Override
            public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Booking(rs.getInt("B_ID"), rs.getString("NOTE"), rs.getInt("USER_ID"),
                        rs.getInt("SERVICE_ID"), rs.getBoolean("IS_DONE"), rs.getDate("BOOKING_DATE"));
            }
        };

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("USERNAME", "%" + username + "%");
        return namedParameterJdbcTemplate.query(sql, paramMap, rowMapper);
    }
    public boolean appointmentIsComplete(int id,boolean is_done) {
        String sql = "UPDATE BOOKING SET IS_DONE = :ID WHERE B_ID = :RANDEVU_ID";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("RANDEVU_ID", id);
        paramMap.put("ID",is_done ? '1' : '0');
        return namedParameterJdbcTemplate.update(sql, paramMap) == 1;
    }


}
