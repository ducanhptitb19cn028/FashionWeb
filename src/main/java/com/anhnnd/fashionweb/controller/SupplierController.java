package com.anhnnd.fashionweb.controller;

import com.anhnnd.fashionweb.model.Admin;
import com.anhnnd.fashionweb.model.Supplier;
import com.anhnnd.fashionweb.service.SupplierService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin-suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public String listSuppliers(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        model.addAttribute("suppliers", suppliers);
        return "listSuppliers";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        model.addAttribute("supplier", new Supplier());
        return "addSupplier";
    }

    @PostMapping("/add")
    public String addSupplier(@ModelAttribute("supplier") Supplier supplier, HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        supplierService.addSupplier(supplier);
        return "redirect:/admin-suppliers";
    }

    @GetMapping("/showEditForm")
    public String showEditForm(@RequestParam("id") Long supplierId, HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        Supplier supplier = supplierService.getSupplierById(supplierId);
        model.addAttribute("supplier", supplier);
        return "editSupplier";
    }

    @PostMapping("/update")
    public String updateSupplier(@ModelAttribute("supplier") Supplier supplier, HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        supplierService.updateSupplier(supplier);
        return "redirect:/admin-suppliers";
    }

    @GetMapping("/showDeleteForm")
    public String showDeleteForm(@RequestParam("id") Long supplierId,HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        Supplier supplier = supplierService.getSupplierById(supplierId);
        model.addAttribute("supplier", supplier);
        return "deleteSupplier";
    }

    @PostMapping("/confirmDelete")
    public String deleteSupplier(@RequestParam("id") Long supplierId, HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        supplierService.deleteSupplier(supplierId);
        return "redirect:/admin-suppliers";
    }

    @GetMapping("/searchByName")
    public String searchSupplierByName(@RequestParam("name") String name, Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        List<Supplier> suppliers = supplierService.searchSuppliersByName(name);
        model.addAttribute("suppliers", suppliers);
        return "listSuppliers";
    }
}