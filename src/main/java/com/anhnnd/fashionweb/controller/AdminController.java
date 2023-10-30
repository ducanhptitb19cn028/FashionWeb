package com.anhnnd.fashionweb.controller;


import com.anhnnd.fashionweb.model.Admin;
import com.anhnnd.fashionweb.repository.AdminRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {
    @Autowired
    private AdminRepository adminRepository;
    @GetMapping("/admin-login")
    public String showLoginAdmin() {
        return "adminlogin";
    }

    @PostMapping("/admin-login")
    public String processLogin(String username, String password, HttpSession session, Model model) {
        Admin admin = adminRepository.findByUsername(username);
        if (admin != null && admin.getPassword().equals(password)) {
            session.setAttribute("admin", admin);
            session.setMaxInactiveInterval(86400);
            model.addAttribute("admin", admin);
            model.addAttribute("name", admin.getUsername());
            return "redirect:/admin";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "adminlogin";
        }
    }
    @GetMapping("/admin")
    public String adminhome(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        return "admin";
    }
    @GetMapping("/admin-logout")
    public String logout(HttpSession session) {
        // Invalidate the session to log the user out
        session.invalidate();
        return "redirect:/admin-login"; // Redirect to the login page after logout
    }
}
