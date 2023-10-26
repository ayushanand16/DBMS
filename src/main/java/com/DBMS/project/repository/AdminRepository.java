package com.DBMS.project.repository;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.DBMS.project.model.Admin;
import com.DBMS.project.model.AdminRowMapper;
import com.DBMS.project.model.Blogs;
import com.DBMS.project.model.Course;
import com.DBMS.project.model.Offer;
import com.DBMS.project.model.Product;


@Repository
public class AdminRepository {
    private final JdbcTemplate jdbcTemplate;

    public AdminRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Admin admin) {
        jdbcTemplate.update("insert into admin (firstName, lastName, email, joiningDate) values (?, ?, ?, ?)", admin.getFirstName(),admin.getLastName(),admin.getEmail(),admin.getJoiningDate());
    }

    public List<Admin> findAll() {
        return jdbcTemplate.query("SELECT * FROM admin",
                new BeanPropertyRowMapper<>(Admin.class));
    }

    @Deprecated
    public List<Admin> findUser(long id) {
        return jdbcTemplate.query("select * from admin where id=?",new Object[]{id},new AdminRowMapper());
    }
    
    @Deprecated
    public List<Blogs> findBlogs(long id) {
        return jdbcTemplate.query("select * from blogs where writer = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Blogs.class));
    }

    @Deprecated
    public List<Product> findProducts(long id) {
        return jdbcTemplate.query("select * from product where maintainer = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Product.class));
    }

    @Deprecated
    public List<Course> findCourses(long id) {
        return jdbcTemplate.query("select * from courses where creator = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Course.class));
    }

    @Deprecated
    public List<Offer> findOffers(long id) {
        return jdbcTemplate.query("select * from offers where maintainer = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Offer.class));
    }
    @Deprecated
    public List<Admin> findByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM admin where email = ?",new Object[]{email}, new AdminRowMapper());
    }


}
