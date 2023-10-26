package com.DBMS.project.model;

import lombok.Data;

@Data
public class CartProduct {
    long clientId;
    long productId;
    String productName;
    String image;
    long cost;
    long quantity;
}
