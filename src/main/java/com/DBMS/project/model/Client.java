package com.DBMS.project.model;

import java.sql.Date;

import lombok.Data;
@Data
public class Client {
    long id;
    String firstName;
    String lastName;
    String emailId;
    String city;
    String state;
    long pinCode;
    String gender;
    Date dob;
}
