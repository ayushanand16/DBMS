package com.DBMS.project.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;




public class ProductRowMapper implements RowMapper<Product> {
    @Deprecated
    public Product mapRow(ResultSet rs, int i) throws SQLException {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setCost(rs.getLong("cost"));
        product.setMaintainer(rs.getLong("maintainer"));
        product.setName(rs.getString("name"));
        product.setStocksAvailable(rs.getLong("stocksAvailable"));
        product.setImage(rs.getString("image"));
        product.setQuantity(rs.getLong("quantity"));
        return product;
    }
}
