package com.anhnnd.fashionweb.controller;

import com.anhnnd.fashionweb.model.Admin;
import com.anhnnd.fashionweb.model.Category;
import com.anhnnd.fashionweb.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin-categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService; // You need to create this service class to handle business logic

    @GetMapping
    public String listCategories(HttpSession session,Model model) throws Exception {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "listCategories";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        model.addAttribute("category", new Category());
        return "addCategory";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute("category") Category category, HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        categoryService.addCategory(category);
        return "redirect:/admin-categories";
    }

    @GetMapping("/showEditForm")
    public String showEditForm(@RequestParam("id") Long categoryId,HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        Category category = categoryService.getCategoryById(categoryId);
        model.addAttribute("category", category);
        return "editCategory";
    }

    @PostMapping("/update")
    public String updateCategory(@ModelAttribute("category") Category category, HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        categoryService.updateCategory(category);
        return "redirect:/admin-categories";
    }

    @GetMapping("/showDeleteForm")
    public String showDeleteForm(@RequestParam("id") Long categoryId,HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        Category category = categoryService.getCategoryById(categoryId);
        model.addAttribute("category", category);
        return "deleteCategory";
    }

    @PostMapping("/confirmDelete")
    public String deleteCategory(@RequestParam("id") Long categoryId, HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        categoryService.deleteCategory(categoryId);
        return "redirect:/admin-categories";
    }

    @GetMapping("/searchByName")
    public String searchCategoryByName(@RequestParam("name") String name, HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        List<Category> categories = categoryService.searchCategoryByName(name);
        model.addAttribute("categories", categories);
        return "listCategories";
    }
}
