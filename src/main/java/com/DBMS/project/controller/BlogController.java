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
import com.DBMS.project.model.Blogs;
import com.DBMS.project.repository.AdminRepository;
import com.DBMS.project.repository.BlogsRepository;
import com.DBMS.project.service.UserInfoDetails;


@Controller
@RequestMapping("/blogs")
public class BlogController {
    private final BlogsRepository blogsRepository;
    private final AdminRepository adminRepository;

    public BlogController(BlogsRepository blogsRepository, AdminRepository adminRepository) {
        this.blogsRepository = blogsRepository;
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
    public String getAllBlogs(Model model, Authentication authentication) {
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
        List<Blogs> blogs = blogsRepository.getAll();
        model.addAttribute("blogs", blogs);
        return "blogs";
    }

    @GetMapping("/{id}")
    @Deprecated
    public String getBlog(Model model, @PathVariable Long id, Authentication authentication) {
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
        Optional<Blogs> blog = blogsRepository.get(id);
        if(blog.isPresent()) {
            model.addAttribute("blog", blog.get());
            return "blog";
        }else {
            return "notfound";
        }
    }

}
