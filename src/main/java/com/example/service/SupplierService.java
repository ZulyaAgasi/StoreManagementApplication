package com.example.service;

import com.example.dto.SupplierResponseDto;
import com.example.entity.Supplier;
import com.example.mapper.SupplierMapper;
import com.example.repository.SupplierRepository;
import com.example.request.SupplierRequest;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Validated
@Transactional(readOnly = true)
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SupplierMapper mapper;

    @Transactional(rollbackFor = Exception.class)
    public SupplierResponseDto createSupplier(@Valid SupplierRequest request) {

        Supplier supplier = new Supplier(UUID.randomUUID(), request.getName(), request.getEmail(), request.getPhone(),
                request.getAddress(), request.getWebsite(), null);

        supplierRepository.saveAndFlush(supplier);

        return mapper.mapToSupplierResponseDto(supplier);

    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteSupplier(UUID id) {

        supplierRepository.deleteById(id);

    }

    public SupplierResponseDto findById(UUID id) {

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow();

        return mapper.mapToSupplierResponseDto(supplier);

    }

    @Transactional(rollbackFor = Exception.class)
    public SupplierResponseDto updateSupplier(UUID id, @Valid SupplierRequest request) {

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow();

        supplier.setName(request.getName());
        supplier.setAddress(request.getAddress());
        supplier.setEmail(request.getEmail());

        supplierRepository.saveAndFlush(supplier);

        return mapper.mapToSupplierResponseDto(supplier);

    }

}
