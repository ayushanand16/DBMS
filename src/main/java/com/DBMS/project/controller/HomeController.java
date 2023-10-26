package com.DBMS.project.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.DBMS.project.model.Admin;
import com.DBMS.project.repository.AdminRepository;
import com.DBMS.project.service.UserInfoDetails;
@Controller
@RequestMapping("/")
public class HomeController {
    private final AdminRepository adminRepository;
    
    public HomeController(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    } 
    @GetMapping
    @Deprecated
    public String HomePage(Authentication authentication, Model model) {
        UserInfoDetails user = (UserInfoDetails) authentication.getPrincipal();
        List<Admin> admin = adminRepository.findByEmail(user.getUsername());
        if(admin.isEmpty()) {
            model.addAttribute("admin",false);
        } else {
            model.addAttribute("admin", true);
            model.addAttribute("adminName", user.getUsername());
        }
        return "home";
    }
}
