package com.DBMS.project.Demonstration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;


@SpringBootApplication
@ComponentScan(basePackages = {"com.DBMS.project.controller", "com.DBMS.project.model","com.DBMS.project.repository","com.DBMS.project.service","com.DBMS.project.filter", "com.DBMS.project.config"})
@EnableJdbcRepositories(basePackages = {"com.DBMS.project.repository"})
@EntityScan(basePackages = {"com.DBMS.project.model"})
public class DemonstrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemonstrationApplication.class, args);
	}

}
