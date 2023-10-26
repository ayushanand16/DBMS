package com.DBMS.project.model;

import lombok.Data;

@Data
public class Blogs {
    long id;
    String description;
    String title;
    String imageLink;
    long writer;
    String name;
}
