package com.DBMS.project.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;



public class BlogRowMapper implements RowMapper<Blogs> {
    public Blogs mapRow(ResultSet rs, int i) throws SQLException {
        Blogs blog = new Blogs();
        blog.setId(rs.getLong("id"));
        blog.setDescription(rs.getString("description"));
        blog.setImageLink(rs.getString("link"));
        blog.setTitle(rs.getString("title"));
        blog.setWriter(rs.getLong("writer_id"));
        blog.setName(rs.getString("firstname")+" "+rs.getString("lastname"));
        return blog;
    }
}
