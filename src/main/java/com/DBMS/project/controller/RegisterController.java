package com.DBMS.project.controller;


import java.sql.Date;
import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.DBMS.project.model.Client;
import com.DBMS.project.model.User;
import com.DBMS.project.repository.CartRepository;
import com.DBMS.project.repository.ClientRepository;
import com.DBMS.project.repository.UserRepository;
import com.DBMS.project.service.UserInfoService;

@Controller
@RequestMapping("/register")
public class RegisterController {
    
    private final UserRepository userRepository;
    private final UserInfoService service;
    private final ClientRepository clientRepository;
    private final CartRepository cartRepository;
    public RegisterController(UserRepository userRepository, UserInfoService service, ClientRepository clientRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.service = service;
        this.clientRepository = clientRepository;
        this.cartRepository = cartRepository;
    }

    // @GetMapping("/admin")
    // public String showAddForm(Model model) {
    //     model.addAttribute("user", new User());
    //     return "register";
    // }

    // @PostMapping("/admin")
    // @Deprecated
    // public String processAddForm(@RequestParam String firstname, @RequestParam String lastname, @RequestParam String email, @RequestParam String password, @RequestParam String confirmPassword) {
    //     if(!(password.equals(confirmPassword))) {
    //         return "register";
    //     }
    //     Optional<User> user2;
    //     user2 = userRepository.findByUserName(email);
    //     if(user2.isPresent()){
    //         return "register";
    //     }
    //     User user = new User();
    //     user.setUsername(email);
    //     user.setEmail(email);
    //     user.setRoles("ROLE_ADMIN");
    //     user.setPassword(confirmPassword);
    //     Admin admin = new Admin();
    //     admin.setEmail(email);
    //     admin.setFirstName(firstname);
    //     admin.setLastName(lastname);
    //     long currentTimeMillis = System.currentTimeMillis();
    //     Date currentDate = new Date(currentTimeMillis);
    //     admin.setJoiningDate(currentDate);
    //     adminRepository.save(admin);
    //     return service.addUser(user);
    // }
    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }
    @GetMapping("/client")
    public String showAddFormClient(Model model) {
        model.addAttribute("user", new Client());
        return "registerClient";
    }
    @PostMapping("/client")
    @Deprecated
    public String processAddFormClient(@RequestParam Date dob, @RequestParam String gender, @RequestParam String firstname, @RequestParam String lastname, @RequestParam String city, @RequestParam String state, @RequestParam long pincode, @RequestParam String email, @RequestParam String password, @RequestParam String confirmPassword) {
        if(isAuthenticated()) {
            return "redirect:/";
        }
        if(!(password.equals(confirmPassword))) {
            return "redirect:/register/client";
        }
        Optional<User> user2;
        user2 = userRepository.findByUserName(email);
        if(user2.isPresent()){
            return "register";
        }
        if(dob.getYear() > 2003) {
            return "register";
        }
        User user = new User();
        user.setUsername(email);
        user.setEmail(email);
        user.setRoles("ROLE_CLIENT");
        user.setPassword(confirmPassword);
        Client client = new Client();
        client.setFirstName(firstname);
        client.setLastName(lastname);
        client.setCity(city);
        client.setEmailId(email);
        client.setState(state);
        client.setPinCode(pincode);
        client.setDob(dob);
        client.setGender(gender);
        clientRepository.save(client);
        Client c2 = clientRepository.findByEmail(email);
        cartRepository.save(c2.getId());
        return service.addUser(user);
    }

    
}
