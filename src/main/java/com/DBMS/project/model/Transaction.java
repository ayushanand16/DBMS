package com.DBMS.project.model;

import java.sql.Date;

import lombok.Data;
@Data
public class Transaction {
    long id;
    Date date;
    long amount;
    long pointsWon;
    long customer;
    long  offer;
}
