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
import com.DBMS.project.model.Course;
import com.DBMS.project.model.Review;
import com.DBMS.project.repository.AdminRepository;
import com.DBMS.project.repository.CourseRepository;
import com.DBMS.project.service.UserInfoDetails;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseRepository courseRepository;
    private final AdminRepository adminRepository;

    public CourseController(CourseRepository courseRepository, AdminRepository adminRepository) {
        this.courseRepository = courseRepository;
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
    public String getAllOffers(Model model, Authentication authentication) {
        List<Course> courses = courseRepository.getAll();
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
        model.addAttribute("courses", courses);
        return "courses";
    }

    @GetMapping("/{id}")
    @Deprecated
    public String getCourse(Model model, @PathVariable Long id, Authentication authentication) {
        Optional<Course> course = courseRepository.getCourse(id);
        if(course.isPresent()) {
            List<Review> reviews = courseRepository.getReviews(course.get());
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
         
            model.addAttribute("reviews", reviews);
            model.addAttribute("course", course.get());
            return "course";
        }else {
            return "notfound";
        }
    }
    
}
