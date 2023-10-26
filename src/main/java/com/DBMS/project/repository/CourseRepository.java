package com.DBMS.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import com.DBMS.project.model.Course;
import com.DBMS.project.model.CourseRowMapper;
import com.DBMS.project.model.Review;

@Repository
public class CourseRepository {
        private final JdbcTemplate jdbcTemplate;

        public CourseRepository(JdbcTemplate jdbcTemplate){
            this.jdbcTemplate = jdbcTemplate;
        }

        @Deprecated
        public Optional<Course> getCourse(Long id) {
            List<Course> course = jdbcTemplate.query("SELECT * FROM courses where id = ?", new Object[]{id}, new CourseRowMapper());
            return (course.isEmpty() ? Optional.empty() : Optional.of(course.get(0)));
        }
        public List<Course> getAll() {
            return jdbcTemplate.query("SELECT * FROM courses", new BeanPropertyRowMapper<>(Course.class));
        }
        public void save(Course course) {
            jdbcTemplate.update("insert into courses (courseName , courseDuration, batchStrength, price, creator) values (?, ?, ?, ?, ?)", 
                                course.getCourseName(),course.getCourseDuration(), course.getBatchStrength(), course.getPrice(), 
                                course.getCreator());
        }

        public void deleteCourse(Course course) {
            jdbcTemplate.update("delete from courses where id = ?",course.getId());
        }

        public void edit(Course courses) {
            jdbcTemplate.update("update courses set  courseName = ?, courseDuration = ?, batchStrength = ?, price = ? where id = ?",courses.getCourseName(),courses.getCourseDuration(),courses.getBatchStrength(),courses.getPrice(),courses.getId());
        }
        @Deprecated
        public List<Review> getReviews(Course course) {
            return jdbcTemplate.query("select creviews.title as title, creviews.description as description, creviews.date as date, client.firstName as firstname, client.lastName as lastname from creviews, client where client.id = creviews.client and creviews.course = ?",new Object[]{course.getId()},new BeanPropertyRowMapper<>(Review.class));
        } 

        public void increaseStudent(Course course) {
            jdbcTemplate.update("update courses set batchStrength = ? where id = ?",course.getBatchStrength()+1,course.getId());
        }

}
