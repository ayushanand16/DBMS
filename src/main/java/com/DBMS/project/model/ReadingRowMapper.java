package com.DBMS.project.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ReadingRowMapper implements RowMapper<Reading> {
    public Reading mapRow(ResultSet rs, int i) throws SQLException{
        Reading reading = new Reading();
        reading.setClient(rs.getLong("client"));
        reading.setDate(rs.getDate("date"));
        reading.setDuration(rs.getInt("duration"));
        reading.setId(rs.getLong("id"));
        reading.setTransaction(rs.getLong("transaction"));
        return reading;
    }
}
