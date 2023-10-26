package com.DBMS.project.model;

import java.sql.Date;

import lombok.Data;
@Data
public class Admin {
    private long id;
    Date joiningDate;
    String firstName;
    String lastName;
    String email;
}
