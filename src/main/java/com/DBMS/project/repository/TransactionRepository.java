package com.DBMS.project.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.DBMS.project.model.Transaction;

@Repository
public class TransactionRepository {
        private final JdbcTemplate jdbcTemplate;

        public TransactionRepository(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }


        public Long save(Transaction yourEntity) {
        String insertQuery = "insert into transaction (amount, customer, date, offer, pointsWon) values (? , ?, ?, ?, ?)";

           KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setLong(1, yourEntity.getAmount());
                preparedStatement.setLong(2, yourEntity.getCustomer());
                preparedStatement.setDate(3, yourEntity.getDate());
                preparedStatement.setLong(4, yourEntity.getOffer());
                preparedStatement.setLong(5, yourEntity.getPointsWon());
                return preparedStatement;
            }
        }, keyHolder);

        // Retrieve the generated key
        Number generatedKey = keyHolder.getKey();
        if(generatedKey != null) {
            return generatedKey.longValue();
        } else {
            return null;
        }

        
    }
    
}
