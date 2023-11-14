package com.anhnnd.fashionweb.controller;

import com.anhnnd.fashionweb.model.*;
import com.anhnnd.fashionweb.service.CategoryService;
import com.anhnnd.fashionweb.service.ProductHistoryService;
import com.anhnnd.fashionweb.service.ProductService;
import com.anhnnd.fashionweb.service.SupplierService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin-products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final SupplierService supplierService;
    private final ProductHistoryService productHistoryService;

    public ProductController(ProductService productService, CategoryService categoryService, SupplierService supplierService, ProductHistoryService productHistoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.supplierService = supplierService;
        this.productHistoryService = productHistoryService;
    }

    @GetMapping
    public String listProducts(Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "listProducts";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        model.addAttribute("product", new Product());

        // Lấy danh sách Category và Supplier
        List<Category> categories = categoryService.getAllCategories();
        List<Supplier> suppliers = supplierService.getAllSuppliers();

        model.addAttribute("categories", categories);
        model.addAttribute("suppliers", suppliers);

        return "addProduct";
    }

    @PostMapping("/addNew")
    public String addNewProduct(@ModelAttribute("product") Product product,
                                HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        productService.addProduct(product);
        return "redirect:/admin-products";
    }



    @PostMapping("/checkProduct")
    public String checkProduct(@RequestParam(value = "name") String name, @RequestParam(value = "size") String size, Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);

        List<Category> categories = categoryService.getAllCategories();
        List<Supplier> suppliers = supplierService.getAllSuppliers();

        model.addAttribute("categories", categories);
        model.addAttribute("suppliers", suppliers);
        Product existingProduct = productService.getProductByNameandSize(name, size);
        model.addAttribute("existingProduct", existingProduct);
        return "addProduct";
    }

    @GetMapping("/showEditForm")
    public String showEditForm(@RequestParam("id") Long productId, Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        List<Category> categories = categoryService.getAllCategories();
        List<Supplier> suppliers = supplierService.getAllSuppliers();

        model.addAttribute("categories", categories);
        model.addAttribute("suppliers", suppliers);
        Product product = productService.getProductById(productId);
        model.addAttribute("product", product);
        return "editProduct";
    }
    @PostMapping("/updateQuantity")
    public String updateQuantity(@RequestParam("id") Long productId,
                                 @RequestParam("quantity") int quantity) {
        Product existingProduct = productService.getProductById(productId);
        existingProduct.setQuantity(existingProduct.getQuantity() + quantity);
        productService.updateProduct(existingProduct);
        productService.saveProductHistory(existingProduct, quantity);
        return "redirect:/admin-products";
    }
    @PostMapping("/update")
    public String updateProduct(@ModelAttribute("product") Product product, HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        productService.updateProduct(product);
        return "redirect:/admin-products";
    }

    @GetMapping("/showDeleteForm")
    public String showDeleteForm(@RequestParam("id") Long productId, Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin) ;
        Product product = productService.getProductById(productId);
        model.addAttribute("product", product);
        return "deleteProduct";
    }

    @PostMapping("/confirmDelete")
    public String deleteProduct(@RequestParam("id") Long productId, HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        productService.deleteProduct(productId);
        return "redirect:/admin-products";
    }

    @GetMapping("/searchByName")
    public String searchProductByName(@RequestParam("name") String name, Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        List<Product> products = productService.searchProductsByName(name);
        model.addAttribute("products", products);
        return "listProducts";
    }

    @GetMapping("/viewAllProductHistory")
    public String viewAllProductHistory(Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        List<ProductHistory> allProductHistory = productHistoryService.getAllProductHistory();
        model.addAttribute("allProductHistory", allProductHistory);
        return "allProductHistory";
    }

    @GetMapping("/viewProductHistory")
    public String viewProductHistory(@RequestParam("id") Long productId, Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        Product product = productService.getProductById(productId);

        List<ProductHistory> productHistory = productHistoryService.getProductHistory(productId);
        model.addAttribute("productHistory", productHistory);
        model.addAttribute("product", product);
        return "productHistory";

    }
}
