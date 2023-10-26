package com.DBMS.project.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class CourseRowMapper implements RowMapper<Course> {
    public Course mapRow(ResultSet rs, int i) throws SQLException {
        Course course = new Course();
        course.setId(rs.getLong("id"));
        course.setCourseName(rs.getString("courseName"));
        course.setBatchStrength(rs.getLong("batchStrength"));
        course.setCourseDuration(rs.getString("courseDuration"));
        course.setPrice(rs.getLong("price"));
        course.setCreator(rs.getLong("creator"));
        return course;
    } 
}
