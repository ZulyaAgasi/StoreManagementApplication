package com.example.controller;

import com.example.dto.SupplierResponseDto;
import com.example.request.SupplierRequest;
import com.example.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/supplier")
@Validated
public class SupplierController {

    @Autowired
    private SupplierService service;

    @PostMapping
    public ResponseEntity<SupplierResponseDto> createSupplier(@Valid @RequestBody SupplierRequest request) {

        SupplierResponseDto supplierResponseDto = service.createSupplier(request);

        return ResponseEntity.ok(supplierResponseDto);

    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> findSupplierById(@PathVariable UUID id) {

        SupplierResponseDto supplier = service.findById(id);

        return ResponseEntity.ok(supplier);

    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> updateSupplier(@PathVariable UUID id, @Valid @RequestBody SupplierRequest request) {

        SupplierResponseDto updatedSupplier = service.updateSupplier(id, request);

        return ResponseEntity.ok(updatedSupplier);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable UUID id) {

        service.deleteSupplier(id);

        return ResponseEntity.noContent().build();

    }

}
