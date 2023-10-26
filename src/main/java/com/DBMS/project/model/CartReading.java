package com.DBMS.project.model;

import java.sql.Date;


import lombok.Data;

@Data
public class CartReading {
    long clientId;
    Date date;
    int  duration;
}
