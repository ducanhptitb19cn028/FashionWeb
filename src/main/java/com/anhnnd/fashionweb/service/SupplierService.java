package com.anhnnd.fashionweb.service;

import com.anhnnd.fashionweb.model.Supplier;
import com.anhnnd.fashionweb.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;


    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }


    public Supplier getSupplierById(Long id) {
        Optional<Supplier> optional = supplierRepository.findById(id);
        Supplier supplier;
        if (optional.isPresent()) {
            supplier = optional.get();
        } else {
            throw new RuntimeException("Supplier not found for id: " + id);
        }
        return supplier;
    }

    @Transactional
    public void addSupplier(Supplier supplier) {
        supplierRepository.save(supplier);
    }

    @Transactional
    public void updateSupplier(Supplier supplier) {
        supplierRepository.save(supplier);
    }

    @Transactional
    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }

    public List<Supplier> searchSuppliersByName(String name) {
        return supplierRepository.findByNameContainingIgnoreCase(name);
    }
}
