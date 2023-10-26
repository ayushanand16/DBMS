package com.DBMS.project.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class CartCourseRowMapper implements RowMapper<CartCourse> {
    public CartCourse mapRow(ResultSet rs ,int i) throws SQLException {
        CartCourse course = new CartCourse();
        course.setClientId(rs.getLong("clientId"));
        course.setCourseId(rs.getLong("courseId"));
        return course;
    }
}
