package com.DBMS.project.repository;


import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.DBMS.project.model.CartCourse;
import com.DBMS.project.model.CartCourseRowMapper;
import com.DBMS.project.model.CartProduct;
import com.DBMS.project.model.CartReading;
import com.DBMS.project.model.Course;
import com.DBMS.project.model.CourseRowMapper;
import com.DBMS.project.model.Product;
import com.DBMS.project.model.ProductRowMapper;




@Repository
public class CartRepository {
    private final JdbcTemplate jdbcTemplate;

    public CartRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public void save(long clientId) {
        jdbcTemplate.update("insert into cart (client) values (?)", clientId);
    }
    public void addProduct(long id, long clientId) {
        jdbcTemplate.update("INSERT INTO Cartproduct (clientId, productId) values (?, ?)",clientId,id);
    }

    public void deleteProduct(long id, long clientId) {
        jdbcTemplate.update("DELETE FROM cartproduct where clientId= ? and productId = ?",clientId,id);
    }

    public void addCourse(long id, long clientId) {
        jdbcTemplate.update("INSERT INTO cartcourse (clientId, courseId) values (? , ?)",clientId,id);
    }

    public void deleteCourse(long id, long clientId) {
        jdbcTemplate.update("DELETE FROM cartcourse where clientId = ? and courseId = ?", clientId,id);
    }

    public void addReading(CartReading reading) {
        jdbcTemplate.update("INSERT INTO cartreading (clientId, date, duration) values (?,?,?)", reading.getClientId(),reading.getDate(),reading.getDuration());
    }

    public void deleteReading(Date id, long clientId) {
        jdbcTemplate.update("DELETE FROM cartreading where clientId = ? and date = ?", clientId,id);
    }
    @Deprecated
    public Optional<CartCourse> viewCourse(long id, long clientId) {
        List<CartCourse> courses = jdbcTemplate.query("SELECT * FROM cartcourse where clientId = ? and courseId = ?", new Object[]{clientId,id}, new CartCourseRowMapper());
        return (courses.isEmpty() ? Optional.empty() : Optional.of(courses.get(0)));
    }
    @Deprecated
    public Optional<CartProduct> viewProduct(long id, long clientId) {
        List<CartProduct> products = jdbcTemplate.query("SELECT * FROM cartproduct where clientId= ? and productId = ?", new Object[]{clientId,id}, new BeanPropertyRowMapper<>(CartProduct.class));
        return (products.isEmpty() ? Optional.empty() : Optional.of(products.get(0)));
    }
    @Deprecated
    public Optional<CartReading> viewReading(long clientId, Date date) {
        List<CartReading> readings = jdbcTemplate.query("select * from cartreading where clientId = ? and date = ?" , new Object[]{clientId,date}, new BeanPropertyRowMapper<>(CartReading.class));
        return (readings.isEmpty() ? Optional.empty() : Optional.of(readings.get(0)));
    }
    public void incrementProduct(CartProduct product) {
        jdbcTemplate.update("UPDATE cartproduct set quantity = ? where productId = ? and clientId = ?", product.getQuantity()+1,product.getProductId(),product.getClientId());
    }
    @Deprecated
    public List<Course> getAllCourses(long clientId) {
        return jdbcTemplate.query("SELECT * FROM courses where id in (SELECT courseId FROM cartcourse where clientId = ?)",new Object[]{clientId},new CourseRowMapper());
    }
    @Deprecated
    public List<Product> getAllProducts(long clientId) {
        return jdbcTemplate.query("SELECT maintainer, stocksAvailable, productId as id, name, image, cost, quantity FROM product, cartproduct where product.id = cartproduct.productId and clientId = ? ",new Object[]{clientId},new ProductRowMapper());
    }
    @Deprecated
    public List<CartReading> getAllReading(long clientId) {
        return jdbcTemplate.query("SELECT * FROM cartreading where clientId = ?",new Object[]{clientId},new BeanPropertyRowMapper<>(CartReading.class));
    }
    @Transactional
    public void deleteCart(long clientId) {
        jdbcTemplate.update("delete from cartreading where clientId = ?",clientId);
        jdbcTemplate.update("delete from cartproduct where clientId = ?", clientId);
        jdbcTemplate.update("delete from cartcourse where clientId = ?", clientId);
    }
}
