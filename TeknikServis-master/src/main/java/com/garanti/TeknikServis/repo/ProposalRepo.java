package com.garanti.TeknikServis.repo;

import com.garanti.TeknikServis.enumeration.Approval;
import com.garanti.TeknikServis.model.Proposal;
import com.garanti.TeknikServis.model.ProposalAdminDto;
import com.garanti.TeknikServis.model.ProposalDto;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class ProposalRepo {
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /* User Metotları: */
    public boolean save (Proposal proposal){
        String sql = "INSERT INTO PROPOSAL(PRP_ID,PRICE, NOTE, PRODUCT_ID, USER_ID, PRP_DATE,APPROVAL) VALUES " +
                "(:PROPOSAL_ID,:PRICE, :NOTE, :PRODUCT_ID, :USER_ID, :PRP_DATE,:APPROVAL)";
        Map<String, Object> paramMap = new HashMap<>();
        proposal.setPRP_ID(getByMaxPRPID()+1);
        paramMap.put("PROPOSAL_ID",proposal.getPRP_ID());
        paramMap.put("PRICE", proposal.getPRICE());
        paramMap.put("NOTE", proposal.getNOTE());
        paramMap.put("PRODUCT_ID", proposal.getPRODUCT_ID());
        paramMap.put("USER_ID", proposal.getUSER_ID());
        paramMap.put("PRP_DATE", LocalDateTime.now()); // date local anlık olarak mı gelsin?
        paramMap.put("APPROVAL", Approval.BEKLEYEN.getValue());

        return namedParameterJdbcTemplate.update(sql,paramMap) == 1;
    }
    public boolean deleteByProposalId(Integer proposalID, Integer userID){
        String sql = "DELETE FROM PROPOSAL WHERE PRP_ID = :PRP_ID AND USER_ID = :USER_ID ";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("USER_ID", userID);
        paramMap.put("PRP_ID", proposalID);

        return namedParameterJdbcTemplate.update(sql,paramMap) == 1;
    }
    public List<ProposalDto> getByUserOffers(Integer id){
        //String sql = "SELECT * FROM PROPOSAL WHERE USER_ID = :USER_ID";
        String sql = "SELECT P.PRP_ID AS ID, P.PRICE AS PRICE, P.NOTE AS NOTE, PR.NAME AS PRODUCT, U.USERNAME AS USERNAME , P.APPROVAL AS APPROVAL, P.PRP_DATE AS TIME FROM PROPOSAL P\n" +
                "INNER JOIN PRODUCT PR ON PR.PRODUCT_ID = P.PRODUCT_ID\n" +
                "INNER JOIN USERS U ON U.USER_ID = P.USER_ID WHERE P.USER_ID = :USER_ID ORDER BY P.PRP_ID";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("USER_ID", id);
        RowMapper<ProposalDto> rowMapper = new RowMapper<ProposalDto>() {
            @Override
            public ProposalDto mapRow(ResultSet result, int rowNum) throws SQLException {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                return new ProposalDto(result.getInt("ID"), result.getInt("PRICE"), result.getString("NOTE"), result.getString("PRODUCT"), result.getString("USERNAME"), Approval.getStringValue(result.getInt("APPROVAL")), sdf.format(result.getTimestamp("TIME")));
            }
        };
        return namedParameterJdbcTemplate.query(sql,paramMap,rowMapper);
        //return namedParameterJdbcTemplate.query(sql,paramMap, BeanPropertyRowMapper.newInstance(Proposal.class));
    }
    public List<ProposalDto> getApprovedOffers(Integer id){
        String sql = "SELECT P.PRP_ID AS ID, P.PRICE AS PRICE, P.NOTE AS NOTE, PR.NAME AS PRODUCT, U.USERNAME AS USERNAME , P.APPROVAL AS APPROVAL, P.PRP_DATE AS TIME FROM PROPOSAL P\n" +
                "INNER JOIN PRODUCT PR ON PR.PRODUCT_ID = P.PRODUCT_ID\n" +
                "INNER JOIN USERS U ON U.USER_ID = P.USER_ID WHERE P.USER_ID = :USER_ID AND P.APPROVAL = :APPROVAL ORDER BY P.PRP_ID";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("USER_ID", id);
        paramMap.put("APPROVAL", Approval.ONAY.getValue());

        RowMapper<ProposalDto> rowMapper = new RowMapper<ProposalDto>() {
            @Override
            public ProposalDto mapRow(ResultSet result, int rowNum) throws SQLException {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                return new ProposalDto(result.getInt("ID"), result.getInt("PRICE"), result.getString("NOTE"), result.getString("PRODUCT"), result.getString("USERNAME"), Approval.getStringValue(result.getInt("APPROVAL")), sdf.format(result.getTimestamp("TIME")));
            }
        };

        return namedParameterJdbcTemplate.query(sql,paramMap, rowMapper);
    }
    public int getByMaxPRPID(){
        String sql = "SELECT MAX(PRP_ID) FROM PROPOSAL";
        return jdbcTemplate.queryForObject(sql,Integer.class);
    }
    public Proposal getProposalById(Integer id){
        String sql = "SELECT * FROM PROPOSAL WHERE PRP_ID = :ID";
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("ID", id);
        return namedParameterJdbcTemplate.queryForObject(sql, paramMap, BeanPropertyRowMapper.newInstance(Proposal.class));
    }
    public List<Proposal> getAll(){
        String sql = "select * from proposal";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Proposal.class));
    }

    public ProposalAdminDto getById(long id){
        String sql = "SELECT P.NAME,PRP.PRICE,PRP.NOTE FROM PROPOSAL PRP INNER JOIN PRODUCT P ON PRP.PRODUCT_ID = P.PRODUCT_ID WHERE PRP.PRP_ID =:ID ";
        Map<String,Object> param = new HashMap<>();
        param.put("ID",id);
        return namedParameterJdbcTemplate.queryForObject(sql,param,BeanPropertyRowMapper.newInstance(ProposalAdminDto.class));
    }

    private Proposal getSpecialById(long id){
        String sql = "SELECT * FROM PROPOSAL WHERE PRP_ID = :ID";
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("ID", id);
        return namedParameterJdbcTemplate.queryForObject(sql,paramMap,BeanPropertyRowMapper.newInstance(Proposal.class));
    }
    public Proposal updateById(long id, int approval){
        String sql = "UPDATE PROPOSAL SET APPROVAL = :APPROVAL WHERE PRP_ID = :ID";
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("APPROVAL", approval);
        paramMap.put("ID", id);
        namedParameterJdbcTemplate.update(sql,paramMap);
        return getSpecialById(id);
    }
    public int infoStatusApproval(long id){
        String sql = "SELECT APPROVAL FROM PROPOSAL WHERE PRP_ID = :ID";
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("ID", id);
        return namedParameterJdbcTemplate.queryForObject(sql,paramMap,Integer.class);

    }
}
