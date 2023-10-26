package com.DBMS.project.model;

import lombok.Data;

@Data
public class Product {
    long id;
    String name;
    String image;
    long stocksAvailable;
    long cost;
    long maintainer;
    long quantity;
}
