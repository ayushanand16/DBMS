package com.DBMS.project.model;

import java.sql.Date;

import lombok.Data;

@Data
public class Review {
    long id;
    long client;
    String title;
    String description;
    long  product;
    Date date;
    String firstname;
    String lastname;
    long course;
}
