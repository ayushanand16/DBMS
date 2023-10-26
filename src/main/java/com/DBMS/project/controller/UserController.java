package com.DBMS.project.controller;



import java.util.List;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;



import com.DBMS.project.model.User;
import com.DBMS.project.repository.UserRepository;



@Controller
@RequestMapping()
public class UserController {
    
    private final UserRepository userRepository;
    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @GetMapping("/users/list")
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "list";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/users/list";
    }

    @GetMapping("/users/{id}")
    @Deprecated
    public String getUserById(@PathVariable Long id, Model model) {
        User user = userRepository.findUser(id);
        if (user != null) {
            model.addAttribute("user", user);
            return "UserDetails"; 
        } else {
            return "UserNotFound"; 
        }
    }

}
