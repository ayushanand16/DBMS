package com.DBMS.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.DBMS.project.model.BlogRowMapper;
import com.DBMS.project.model.Blogs;

@Repository
public class BlogsRepository {

    private final JdbcTemplate jdbcTemplate;

    public BlogsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Blogs> getAll() {
        return jdbcTemplate.query("SELECT blogs.title as title, blogs.description as description, blogs.id as id, admin.firstName as firstname, admin.lastName as lastname, blogs.imageLink as link, admin.id as writer_id  FROM blogs, admin where blogs.writer = admin.id", new BlogRowMapper());
    }
    @Deprecated
    public Optional<Blogs> get(Long id) {
        List<Blogs> blogs= jdbcTemplate.query("select blogs.title as title, blogs.description as description, blogs.id as id, admin.firstName as firstname, admin.lastName as lastname, blogs.imageLink as link, admin.id as writer_id from blogs, admin where blogs.id=? and admin.id = blogs.writer",new Object[]{id},new BlogRowMapper());
        return (blogs.isEmpty() ? Optional.empty() : Optional.of(blogs.get(0)));
    }

    public void save(Blogs blogs) {
        jdbcTemplate.update("insert into blogs (description, title, imageLink, writer) values (?,?,?,?)",blogs.getDescription(),blogs.getTitle(),blogs.getImageLink(),blogs.getWriter());
    }

    public void delete(Blogs blog) {
        jdbcTemplate.update("delete from blogs where id = ?",blog.getId());
    }

    public void edit(Blogs blogs) {
        jdbcTemplate.update("update blogs set description = ?, title = ?, imageLink = ? where id = ?",blogs.getDescription(),blogs.getTitle(),blogs.getImageLink(),blogs.getId());
    }
    
}
