package com.DBMS.project.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class AdminRowMapper implements RowMapper<Admin> {
    public Admin mapRow(ResultSet resultSet, int i) throws SQLException {

		Admin person = new Admin();
		person.setId(resultSet.getLong("id"));
		person.setJoiningDate(resultSet.getDate("joiningDate"));
        person.setFirstName(resultSet.getString("firstName"));
        person.setLastName(resultSet.getString("lastName"));
		person.setEmail(resultSet.getString("email"));
		return person;
	}
}
