package com.example.mapper;

import com.example.dto.SupplierResponseDto;
import com.example.entity.Supplier;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    public SupplierResponseDto mapToSupplierResponseDto(Supplier supplier) {

        return new SupplierResponseDto(
                supplier.getId(),
                supplier.getName(),
                supplier.getEmail(),
                supplier.getPhone(),
                supplier.getAddress(),
                supplier.getWebsite(),
                supplier.getUpdatedAt());
    }

}
