package com.DBMS.project.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.DBMS.project.model.Admin;
import com.DBMS.project.model.Product;
import com.DBMS.project.model.Review;
import com.DBMS.project.repository.AdminRepository;
import com.DBMS.project.repository.ProductRepository;
import com.DBMS.project.service.UserInfoDetails;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;
    public ProductController(ProductRepository productRepository, AdminRepository adminRepository) {
        this.productRepository = productRepository;
        this.adminRepository = adminRepository;
    }
    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    @GetMapping("/all")
    @Deprecated
    public String getProduct(Model model, Authentication authentication) {
        if((isAuthenticated())) {
            UserInfoDetails user = (UserInfoDetails)authentication.getPrincipal();
            List<Admin> admin = adminRepository.findByEmail(user.getUsername());
         if(admin.isEmpty()) {
            model.addAttribute("admin",false);
         } else {
            model.addAttribute("admin", true);
         }
        }else {
            model.addAttribute("admin", false);
        }
        List<Product> products = productRepository.getAll();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/{id}")
    @Deprecated
    public String getProduct(Model model, @PathVariable Long id, Authentication authentication) {
        Optional<Product> product = productRepository.getProduct(id);
        if((isAuthenticated())) {
            UserInfoDetails user = (UserInfoDetails)authentication.getPrincipal();
            List<Admin> admin = adminRepository.findByEmail(user.getUsername());
         if(admin.isEmpty()) {
            model.addAttribute("admin",false);
         } else {
            model.addAttribute("admin", true);
         }
        } else {
            model.addAttribute("admin", false);
        }
        if(product.isPresent()) {
            model.addAttribute("product", product.get());
            List<Review> reviews = productRepository.getReviews(product.get());
            model.addAttribute("reviews", reviews);
            return "product";
        }else {
            return "notfound";
        }
    }
}
