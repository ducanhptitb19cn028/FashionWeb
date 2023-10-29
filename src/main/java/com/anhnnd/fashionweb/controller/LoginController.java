package com.anhnnd.fashionweb.controller;

import com.anhnnd.fashionweb.model.User;
import com.anhnnd.fashionweb.repository.UserRepository;
import com.anhnnd.fashionweb.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginPage(Model model) {
//        model.getAttribute("error");
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(String email,String password, HttpSession session, Model model) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            // Đăng nhập thành công, lưu thông tin vào session
            session.setAttribute("user", user);
            model.addAttribute("username", user.getLastname());
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "/login";
        }
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        model.getAttribute("registrationError");
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid User user,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        if(!user.getPassword().equals(user.getConfirmPassword())){
            model.addAttribute("registrationError", "Confirm Password is not correct");
            return "register";
        }
        // Check if email or username already exists
        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute("registrationError", "Email or username already exists.");
            return "register";
        }

        // Save the new user
        userService.save(user);

        return "redirect:/login"; // Redirect to the login page after successful registration
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Invalidate the session to log the user out
        session.invalidate();
        return "redirect:/"; // Redirect to the home page after logout
    }
}