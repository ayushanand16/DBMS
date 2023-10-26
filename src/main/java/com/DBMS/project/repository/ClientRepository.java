package com.DBMS.project.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.DBMS.project.model.CartReading;
import com.DBMS.project.model.Client;
import com.DBMS.project.model.ClientRowMapper;
import com.DBMS.project.model.Course;
import com.DBMS.project.model.CourseRowMapper;
import com.DBMS.project.model.Offer;
import com.DBMS.project.model.Product;
// import com.DBMS.project.model.ProductRowMapper;
import com.DBMS.project.model.Reading;
import com.DBMS.project.model.Review;

@Repository
public class ClientRepository {
    private final JdbcTemplate jdbcTemplate;

    public ClientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Deprecated
    public Client findClient(long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM client WHERE id = ?", new Object[] { id },
                new ClientRowMapper());
    }

    @Deprecated
    public Client findByEmail(String email) {
        return jdbcTemplate.queryForObject("SELECT * FROM  client WHERE email = ?", new Object[] { email },
                new ClientRowMapper());
    }

    public List<Client> findAll() {
        return jdbcTemplate.query("select * from client", new BeanPropertyRowMapper<>(Client.class));
    }

    public void Delete(long id) {
        jdbcTemplate.update("delete from client where id = ?", id);
    }

    @Deprecated
    public List<Reading> findReadings(long id) {
        return jdbcTemplate.query(
                "SELECT * FROM readings where transaction in (select id from transaction where customer = ?)",
                new Object[] { id }, new BeanPropertyRowMapper<>(Reading.class));
    }

    @Deprecated
    public List<Course> findCourses(long id) {
        return jdbcTemplate.query(
                "select * from courses where courses.id in (select distinct course from enrollment where transaction in (select id from transaction where customer = ?))",
                new Object[] { id }, new BeanPropertyRowMapper<>(Course.class));
    }

    @Deprecated
    public List<Product> findProducts(long id) {
        return jdbcTemplate.query(
                "SELECT * FROM product where product.id in (SELECT DISTINCT productId as id FROM soldProduct where transactionId in (SELECT id from transaction where customer = ?))",
                new Object[] { id }, new BeanPropertyRowMapper<>(Product.class));
    }

    // @Deprecated
    // public Product getProduct(long id, long clientId) {
    // return jdbcTemplate.queryForObject("SELECT * FROM product where id in (SELECT
    // DISTINCT productId as id FROM soldProduct where transactionId in (SELECT id
    // from transaction where client = ?) and productId = ?)",
    // new Object[]{clientId,id}, new ProductRowMapper());
    // }
    @Deprecated
    public Optional<Course> getCourse(long id, long clientId) {
        List<Course> courses = jdbcTemplate.query(
                "SELECT * FROM courses where id in (SELECT course FROM enrollment where transaction in (select id from transaction where customer = ?) and course = ?)",
                new Object[] { clientId, id }, new CourseRowMapper());
        return (courses.isEmpty() ? Optional.empty() : Optional.of(courses.get(0)));
    }

    @Deprecated
    public Optional<Product> getProduct(long id, long clientId) {
        List<Product> products = jdbcTemplate.query(
                "SELECT * FROM product where product.id = ? and product.id in (SELECT DISTINCT productId as id FROM soldProduct where transactionId in (SELECT id from transaction where customer = ?))",
                new Object[] { id, clientId }, new BeanPropertyRowMapper<>(Product.class));
        return (products.isEmpty() ? Optional.empty() : Optional.of(products.get(0)));
    }

    public void save(Client client) {
        jdbcTemplate.update(
                "INSERT INTO client (firstname, lastname, city, state, pincode, gender, dob, email) values (?, ?, ?, ?, ?, ?, ?, ?)",
                client.getFirstName(), client.getLastName(), client.getCity(), client.getState(), client.getPinCode(),
                client.getGender(),
                client.getDob(), client.getEmailId());
    }

    @Deprecated
    public List<Offer> getOffers(long id) {
        return jdbcTemplate.query(
                "select * from offers where pointsRequired <= (select sum(pointsWon) from transaction where date >= NOW() - INTERVAL 10 DAY and date <= NOW() group by customer having customer = ?)",
                new Object[] { id }, new BeanPropertyRowMapper<>(Offer.class));
    }

    public void addCourse(long id, Course course) {
        jdbcTemplate.update("insert into enrollment (transaction,course) values (?,?)", id, course.getId());
    }

    public void addProduct(long id, Product product) {
        jdbcTemplate.update("insert into soldProduct (transactionId, productId) values (?,?)", id, product.getId());
    }

    public void addReading(CartReading reading, long key) {
        jdbcTemplate.update("insert into readings (date, duration, transaction) values (?,?,?)", reading.getDate(),
                reading.getDuration(), key);
    }

    public void addProductReview(Review review) {
        jdbcTemplate.update("insert into reviews (client, product, date, title, description) values (?,?,now(),?,?)",
                review.getClient(), review.getProduct(), review.getTitle(), review.getDescription());
    }

    public void addCourseReview(Review review) {
        jdbcTemplate.update("insert into creviews (client, course, date, title, description) values (?,?,now(),?,?)",
                review.getClient(), review.getCourse(), review.getTitle(), review.getDescription());
    }

    @Deprecated
    public Optional<Reading> viewReading(long id, Date date) {
        List<Reading> readings = jdbcTemplate.query("select * from readings, transaction where readings.transaction = transaction.id and transaction.customer = ? and readings.date = ?",
                new Object[] { id, date }, new BeanPropertyRowMapper<>(Reading.class));

        return (readings.isEmpty() ? Optional.empty() : Optional.of(readings.get(0)));
    }

}
