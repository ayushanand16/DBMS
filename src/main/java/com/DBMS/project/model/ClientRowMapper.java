package com.DBMS.project.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ClientRowMapper implements RowMapper<Client> {
    public Client mapRow(ResultSet rs, int i) throws SQLException {
        Client client = new Client();
        client.setId(rs.getLong("id"));
        client.setFirstName(rs.getString("firstname"));
        client.setLastName(rs.getString("lastname"));
        client.setCity(rs.getString("city"));
        client.setDob(rs.getDate("dob"));
        client.setEmailId(rs.getString("email"));
        client.setGender(rs.getString("gender"));
        client.setPinCode(rs.getLong("pincode"));
        client.setState(rs.getString("state"));
        return client;
    }
}
