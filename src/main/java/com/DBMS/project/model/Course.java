package com.DBMS.project.model;

import lombok.Data;

@Data
public class Course {
    long id;
    String courseName;
    String courseDuration;
    long batchStrength;
    long price;
    long creator;
}
