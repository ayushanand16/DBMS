package com.DBMS.project.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TransactionRowMapper implements RowMapper<Transaction> {
    public Transaction mapRow(ResultSet rs, int i) throws SQLException{
        Transaction transaction = new Transaction();
        transaction.setId(rs.getLong("id"));
        transaction.setAmount(rs.getLong("amount"));
        transaction.setCustomer(rs.getLong("customer"));
        transaction.setDate(rs.getDate("date"));
        transaction.setOffer(rs.getLong("offer"));
        transaction.setPointsWon(rs.getLong("pointsWon"));
        return transaction;
    }
 }
