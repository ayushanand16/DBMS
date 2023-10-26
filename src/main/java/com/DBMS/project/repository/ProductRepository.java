package com.DBMS.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.DBMS.project.model.Product;
import com.DBMS.project.model.Review;

@Repository
public class ProductRepository {
    private final JdbcTemplate jdbcTemplate;
    public ProductRepository(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> getAll() {
        return jdbcTemplate.query("SELECT * FROM product", new BeanPropertyRowMapper<>(Product.class));
    }
    @Deprecated
    public Optional<Product> getProduct(Long id) {
        List<Product> product = jdbcTemplate.query("SELECT * FROM product where id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Product.class));
        return (product.isEmpty() ? Optional.empty() : Optional.of(product.get(0)));
    }


    public void save(Product product) {
        jdbcTemplate.update("INSERT INTO product (cost, maintainer, name, stocksAvailable, image) values (?, ?, ?, ?, ?)",product.getCost(),product.getMaintainer(),product.getName(),product.getStocksAvailable(), product.getImage());
    }

    public void deleteProduct(Product product) {
        jdbcTemplate.update("DELETE FROM product where id = ?", product.getId());
    }

    public void edit(Product product) {
        jdbcTemplate.update("UPDATE product SET name = ?, image = ?, stocksAvailable = ?, cost = ? where id = ?",product.getName(),product.getImage(),product.getStocksAvailable(),product.getCost(), product.getId());
    }
    @Deprecated
    public List<Review> getReviews(Product product) {
        return jdbcTemplate.query("select reviews.title as title, reviews.description as description, reviews.date as date, client.firstName as firstname, client.lastName as lastname from reviews, client where client.id = reviews.client and reviews.product = ?",new Object[]{product.getId()},new BeanPropertyRowMapper<>(Review.class));
    }
    public void decrement(Product product) {
        jdbcTemplate.update("update product set stocksAvailable = ? where id = ?",product.getStocksAvailable()-product.getQuantity(),product.getId());
    }
    
}
