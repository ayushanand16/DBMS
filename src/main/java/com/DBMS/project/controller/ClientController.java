package com.DBMS.project.controller;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.DBMS.project.model.CartCourse;
import com.DBMS.project.model.CartProduct;
import com.DBMS.project.model.CartReading;
import com.DBMS.project.model.Client;
import com.DBMS.project.model.Course;
import com.DBMS.project.model.Offer;
import com.DBMS.project.model.Product;
import com.DBMS.project.model.Reading;
import com.DBMS.project.model.Review;
import com.DBMS.project.repository.CartRepository;
import com.DBMS.project.repository.ClientRepository;
import com.DBMS.project.repository.ProductRepository;
import com.DBMS.project.service.UserInfoDetails;


@Controller
@RequestMapping("/clients")
public class ClientController {
    private final ClientRepository clientRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    public ClientController(ClientRepository clientRepository, CartRepository cartRepository, ProductRepository productRepository) {
        this.clientRepository = clientRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }
    @Deprecated
    @GetMapping("/orders")
    public String order(Model model, Authentication authentication){
        UserInfoDetails client = (UserInfoDetails) authentication.getPrincipal();
        Client c = clientRepository.findByEmail(client.getUsername());
        List<Product> products = clientRepository.findProducts(c.getId());
        model.addAttribute("products", products);
        List<Reading> readings = clientRepository.findReadings(c.getId());
        model.addAttribute("readings",readings);
        List<Course> courses = clientRepository.findCourses(c.getId());
        model.addAttribute("courses", courses);
        System.out.println(products);
        return "orders";
    }
    @PostMapping("/products/add/{id}")
    @Deprecated
    public String cartProductAdd(@PathVariable long id, Authentication authentication) {
            UserInfoDetails c = (UserInfoDetails) authentication.getPrincipal();
            Client client = clientRepository.findByEmail(c.getUsername());
            Optional<CartProduct> product = cartRepository.viewProduct(id,client.getId());
            if(!(product.isPresent())) {
                cartRepository.addProduct(id, client.getId());
            }
            else {
                Optional<Product> p = productRepository.getProduct(product.get().getProductId());
                if(p.get().getStocksAvailable()>product.get().getQuantity()+1)
                cartRepository.incrementProduct(product.get());
            }
            return "redirect:/clients/cart";
    }
    @PostMapping("/products/delete/{id}")
    @Deprecated
    public String cartProductDelete(@PathVariable long id, Authentication authentication) {
            UserInfoDetails c = (UserInfoDetails) authentication.getPrincipal();
            Client client = clientRepository.findByEmail(c.getUsername());
            Optional<CartProduct> product =  cartRepository.viewProduct(id,client.getId());
            System.out.println(id);
            System.out.println(client.getId());
            if(product.isPresent()) {
                System.out.print(product);
                cartRepository.deleteProduct(id, client.getId());   
            }
            return "redirect:/clients/cart";
    }
    @PostMapping("/courses/add/{id}")
    @Deprecated
    public String cartCourseAdd(@PathVariable long id, Authentication authentication) {
            UserInfoDetails c = (UserInfoDetails) authentication.getPrincipal();
            Client client = clientRepository.findByEmail(c.getUsername());
            Optional<CartCourse> course = cartRepository.viewCourse(id, client.getId());
            Optional<Course> scourse = clientRepository.getCourse(id,client.getId());
            if(scourse.isPresent()) {
                return "redirect:/courses/"+id;
            }
            if(!(course.isPresent()))
            cartRepository.addCourse(id,client.getId());
            return "redirect:/clients/cart";
    }
    @PostMapping("/courses/delete/{id}")
    @Deprecated
    public String cartCourseDelete(@PathVariable long id, Authentication authentication) {
            UserInfoDetails c = (UserInfoDetails) authentication.getPrincipal();
            Client client = clientRepository.findByEmail(c.getUsername());
            Optional<CartCourse> course =  cartRepository.viewCourse(id, client.getId());
            if(course.isPresent())
            cartRepository.deleteCourse(id,client.getId());
            return "redirect:/clients/cart";
    }
    @PostMapping("/readings/add")
    @Deprecated
    public String cartReadingAdd(@ModelAttribute CartReading reading, Authentication authentication) {
            UserInfoDetails c = (UserInfoDetails) authentication.getPrincipal();
            Client client = clientRepository.findByEmail(c.getUsername());
            Optional<CartReading> readings = cartRepository.viewReading(client.getId(),reading.getDate()) ;
            if(readings.isPresent()) {
                return "redirect:/clients/cart";
            }
            long currentTimeMillis = System.currentTimeMillis();
            Date currentDate = new Date(currentTimeMillis);
            if(reading.getDate().before(currentDate) || reading.getDuration() > 5) {
                return "redirect:/clients/readings/add";
            }
            Optional<Reading> read = clientRepository.viewReading(client.getId(),reading.getDate());
            if(read.isPresent()) {
                return "redirect:/clients/orders";
            }
            reading.setClientId(client.getId());
            cartRepository.addReading(reading);
            return "redirect:/clients/cart";
    }
    @PostMapping("/readings/delete/{id}")
    @Deprecated
    public String cartReadingDelete(@PathVariable Date id, Authentication authentication) {
            UserInfoDetails c = (UserInfoDetails) authentication.getPrincipal();
            Client client = clientRepository.findByEmail(c.getUsername());
            cartRepository.deleteReading(id, client.getId());
            return "redirect:/clients/cart";
    }
    @GetMapping("/cart")
    @Deprecated
    public String getCart(Model model, Authentication authentication) {
            UserInfoDetails c = (UserInfoDetails) authentication.getPrincipal();
            Client client = clientRepository.findByEmail(c.getUsername());
            List<Course> courses = cartRepository.getAllCourses(client.getId());
            model.addAttribute("courses",courses);
            List<CartReading> readings = cartRepository.getAllReading(client.getId());
            model.addAttribute("readings", readings);
            List<Product> products = cartRepository.getAllProducts(client.getId());
            model.addAttribute("products", products);
            long sum = 0;
            for(int i=0 ; i<courses.size() ; i++) {
                sum += courses.get(i).getPrice();
            }
            for(int i=0 ; i<products.size() ; i++) {
                sum += products.get(i).getCost()*products.get(i).getQuantity();
            }
            for(int i=0 ; i <readings.size() ; i++) {
                sum += readings.get(i).getDuration()*100;
            }
            model.addAttribute("sum", sum);
            List<Offer> offers = clientRepository.getOffers(client.getId());
            model.addAttribute("offers", offers);
            return "cart";
    }
    @GetMapping("/readings/add")
    @Deprecated
    public String addReading(Model model, Authentication authentication) {
        UserInfoDetails c = (UserInfoDetails) authentication.getPrincipal();
        Client client = clientRepository.findByEmail(c.getUsername());
        model.addAttribute("client", client);
        return "addReading";
    }

    @GetMapping("/products/review/{id}")
    @Deprecated
    public String addProductReview(Model model, @PathVariable long id, Authentication authentication) {
        model.addAttribute("Course", false);
        UserInfoDetails c = (UserInfoDetails) authentication.getPrincipal();
            Client client = clientRepository.findByEmail(c.getUsername());
        Optional<Product> product = clientRepository.getProduct(id, client.getId()); 
        if(!product.isPresent()) {
            return "redirect:/products/all";
        }
        model.addAttribute("product", product.get());
        return "review";
    }
    @GetMapping("/courses/review/{id}")
    @Deprecated
    public String addCourseReview(Model model, @PathVariable long id, Authentication authentication) {
        model.addAttribute("Course", true);
        UserInfoDetails c = (UserInfoDetails) authentication.getPrincipal();
            Client client = clientRepository.findByEmail(c.getUsername());
        Optional<Course> course = clientRepository.getCourse(id, client.getId()); 
        if(!course.isPresent()) {
            return "redirect:/courses/all";
        }
        model.addAttribute("course", course.get());
        return "review";
    }

    @PostMapping("/products/review/{id}")
    @Deprecated
    public String addProductReviewPost(@RequestParam String title, @RequestParam String description, Authentication authentication, @PathVariable long id) {
        UserInfoDetails c = (UserInfoDetails) authentication.getPrincipal();
         Client client = clientRepository.findByEmail(c.getUsername());
        Review review = new Review();
        review.setClient(client.getId());
        review.setProduct(id);
        review.setDescription(description);
        review.setTitle(title);
        clientRepository.addProductReview(review);

        return "redirect:/products/"+id;
    }

    @PostMapping("/courses/review/{id}")
    @Deprecated
    public String addCourseReviewPost(@RequestParam String title, @RequestParam String description, Authentication authentication, @PathVariable long id) {
        UserInfoDetails c = (UserInfoDetails) authentication.getPrincipal();
         Client client = clientRepository.findByEmail(c.getUsername());
        Review review = new Review();
        review.setClient(client.getId());
        review.setCourse(id);
        review.setDescription(description);
        review.setTitle(title);
        clientRepository.addCourseReview(review);

        return "redirect:/courses/"+id;
    }
    

    
}
