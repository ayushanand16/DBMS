package com.DBMS.project.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ReviewRowMapper implements RowMapper<Review> {
    public Review mapRow(ResultSet rs, int i) throws SQLException {
        Review review = new Review();
        review.setId(rs.getLong("id"));
        review.setTitle(rs.getString("title"));
        review.setClient(rs.getLong("client"));
        review.setDescription(rs.getString("description"));
        review.setProduct(rs.getLong("product"));
        return review;
    }
}
