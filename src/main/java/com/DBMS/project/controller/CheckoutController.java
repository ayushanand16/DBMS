package com.DBMS.project.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.DBMS.project.model.CartReading;
import com.DBMS.project.model.Client;
import com.DBMS.project.model.Course;
import com.DBMS.project.model.Offer;
import com.DBMS.project.model.Product;
import com.DBMS.project.model.Transaction;
import com.DBMS.project.repository.CartRepository;
import com.DBMS.project.repository.ClientRepository;
import com.DBMS.project.repository.CourseRepository;
import com.DBMS.project.repository.OfferRepository;
import com.DBMS.project.repository.ProductRepository;
import com.DBMS.project.repository.TransactionRepository;
import com.DBMS.project.service.UserInfoDetails;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Controller
@RequestMapping
public class CheckoutController {

    private final ClientRepository clientRepository;
    private final CartRepository cartRepository;
    private final TransactionRepository transactionRepository;
    private final OfferRepository offerRepository;
    private final CourseRepository courseRepository;
    private final ProductRepository productRepository;

    public CheckoutController(ClientRepository clientRepository, CartRepository cartRepository,
            TransactionRepository transactionRepository, OfferRepository offerRepository, ProductRepository productRepository, CourseRepository courseRepository) {
        this.cartRepository = cartRepository;
        this.clientRepository = clientRepository;
        this.transactionRepository = transactionRepository;
        this.offerRepository = offerRepository;
        this.courseRepository = courseRepository;
        this.productRepository = productRepository;
    }

    @PostMapping("/create-checkout-session")
    @Deprecated
    public String Checkout(@RequestParam long sum, @RequestParam long offer) {
        Stripe.apiKey = "sk_test_51O3kWzSFlvMw58q9quPxfZPMdy0kKzZx83Z9LGHs40fPAgmJYCgE0oXuxIXt3DGagx1srmwg8muwViLDjxU0n6BV00h0thCTsb";
        String success = "http://localhost:8080/success/" + sum + "/" + offer + "?session_id={CHECKOUT_SESSION_ID}";
        String cancel = "http://localhost:8080/clients/cart";
        Optional<Offer> offerM = offerRepository.getOffer(offer);
        Long price = sum - ((sum*offerM.get().getDiscount())/100);
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(success)
                .setCancelUrl(cancel)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("inr")
                                                .setUnitAmount(price*100)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("order")
                                                                .build())
                                                .build())
                                .build())
                .build();

        try {
            Session session = Session.create(params);

            String url = session.getUrl();

            return "redirect:" + url;

        } catch (StripeException e) {
            System.out.println(e);
            return "redirect:/";
        }

    }

    @GetMapping("/success/{sum}/{offer}")
    @Deprecated
    @Transactional
    public String sucess(Model model, Authentication authentication, @PathVariable long sum, @PathVariable long offer,
            @RequestParam String session_id) {
        try {
            Session session = Session.retrieve(session_id);
            if (session.getPaymentStatus().equals("paid")) {
                UserInfoDetails c = (UserInfoDetails) authentication.getPrincipal();
                Client client = clientRepository.findByEmail(c.getUsername());
                List<Course> courses = cartRepository.getAllCourses(client.getId());
                model.addAttribute("courses", courses);
                List<CartReading> readings = cartRepository.getAllReading(client.getId());
                model.addAttribute("readings", readings);
                List<Product> products = cartRepository.getAllProducts(client.getId());
                model.addAttribute("products", products);
                cartRepository.deleteCart(client.getId());
                Transaction transaction = new Transaction();
                transaction.setCustomer(client.getId());
                transaction.setAmount(sum);
                transaction.setPointsWon(sum / 100);
                transaction.setOffer(offer);
                java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                transaction.setDate(date);
                long key = transactionRepository.save(transaction);
                for (int i = 0; i < courses.size(); i++) {
                    clientRepository.addCourse(key, courses.get(i));
                    courseRepository.increaseStudent(courses.get(i));
                }
                for (int i = 0; i < products.size(); i++) {
                    clientRepository.addProduct(key, products.get(i));
                    productRepository.decrement(products.get(i));
                }
                for (int i = 0; i < readings.size(); i++) {
                    System.out.print("hey\n\n\n\n\n\n");
                    clientRepository.addReading(readings.get(i), key);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return "redirect:/clients/orders";
    }
}
