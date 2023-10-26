package com.DBMS.project.model;

import java.sql.Date;

import lombok.Data;

@Data
public class Reading {
    long id;
    Date date;
    int  duration;
    long client;
    long transaction;
}
