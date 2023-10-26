package com.DBMS.project.repository;

import org.springframework.stereotype.Repository;

import com.DBMS.project.model.User;
import com.DBMS.project.model.UserRowMapper;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;



import java.util.List;
import java.util.Optional;
@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;


    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(User user) {
        jdbcTemplate.update("INSERT INTO users (username, email, password, roles) VALUES (?, ?, ?, ?)",
                user.getUsername(), user.getEmail(), user.getPassword(), user.getRoles());
    }

    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM user",
                new BeanPropertyRowMapper<>(User.class));
    }
    
    @Deprecated
    public User findUser(long id) {
        User user = new User();
        user =jdbcTemplate.queryForObject("select * from users where id=?",new Object[]{id},new UserRowMapper());
        return user;
    }


    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }
    @Deprecated
    public Optional<User> findByUserName(String name) {
        System.out.println(name);
        List<User> users = jdbcTemplate.query("SELECT * FROM users where username = ?", new Object[]{name}, new UserRowMapper());
        System.out.println(users.size());
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }
}
