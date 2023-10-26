package com.DBMS.project.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper implements RowMapper<User> {
    public User mapRow(ResultSet resultSet, int i) throws SQLException {

		User person = new User();
		person.setId(resultSet.getLong("id"));
		person.setUsername(resultSet.getString("username"));
        person.setEmail(resultSet.getString("email"));
		person.setPassword(resultSet.getString("password"));
		person.setRoles(resultSet.getString("roles"));
		return person;
	}
}
