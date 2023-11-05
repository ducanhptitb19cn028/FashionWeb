package com.anhnnd.fashionweb.controller;


import com.anhnnd.fashionweb.model.Category;
import com.anhnnd.fashionweb.model.Product;
import com.anhnnd.fashionweb.model.User;
import com.anhnnd.fashionweb.service.CategoryService;
import com.anhnnd.fashionweb.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String homepage(Model model, HttpSession session) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("allproducts", productService.getAllProducts());
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/category")
    public String viewAllCategories(Model model, HttpSession session) {
        model.addAttribute("categories", categoryService.getAllCategories());
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/products")
    public String viewAllProductByCategory(Model model, @RequestParam("cid") Long id, HttpSession session) {
        model.addAttribute("categories", categoryService.getAllCategories());
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        if (id != null) {
            model.addAttribute("prodByCate", productService.getProductByCategoryId(id));
            Category selectedCategory = categoryService.getCategoryById(id);
            model.addAttribute("selectedCategoryName", selectedCategory.getName());
        } else {
            model.addAttribute("allproducts", productService.getAllProducts());
        }
        return "home";
    }

    @GetMapping("/productDetails")
    public String viewProductDetails(Model model, @RequestParam("id") Long productId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        Product product = productService.getProductById(productId);
        model.addAttribute("product", product);
        return "productDetails";
    }
}
