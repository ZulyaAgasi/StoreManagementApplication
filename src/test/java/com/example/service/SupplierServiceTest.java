package com.example.service;

import com.example.TestContainerInitialization;
import com.example.dto.StoreResponseDto;
import com.example.dto.SupplierResponseDto;
import com.example.entity.Store;
import com.example.entity.Supplier;
import com.example.repository.SupplierRepository;
import com.example.request.StoreRequest;
import com.example.request.SupplierRequest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootTest
class SupplierServiceTest extends TestContainerInitialization {

    @Autowired
    private SupplierRepository repository;

    @Autowired
    private SupplierService service;

    @AfterEach
    void clear() {
        repository.deleteAll();
    }

    @ParameterizedTest
    @MethodSource("dataBlank")
    void createSupplier_whenDataBlank_thenThrow(String name, String email, String address, String phone, String website) {

        SupplierRequest supplierRequest = createSupplierRequest(name, email, address, phone, website);

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.createSupplier(supplierRequest));

    }

    @Test
    void createSupplier_whenDataNotBlank_thenCreateSupplier() {

        SupplierRequest supplierRequest = createSupplierRequest("Zewa", "zewa@mail.ru", "ул.Сосновая", "56845", "https://zewa-russia.ru/");

        SupplierResponseDto supplierResponseDto = Assertions.assertDoesNotThrow(() -> service.createSupplier(supplierRequest));

        Assertions.assertEquals(supplierRequest.getName(), supplierResponseDto.getName());

    }

//    @ParameterizedTest
//    @MethodSource("invalidData")
//    void updateSupplier_whenRequestInvalid_thenThrow(String name, String location, String email, String address, String phone, String website) {
//
//        Supplier supplier = createSupplier("Zewa", "zewa@mail.ru", "ул.Сосновая", "56845", "https://zewa-russia.ru/");
//
//        SupplierRequest supplierRequest = createSupplierRequest(name, location, email, phone, address, website);
//
//        Assertions.assertThrows(ConstraintViolationException.class, () -> service.updateSupplier(supplier.getId(), supplierRequest));
//
//    }

//    private Supplier createSupplier(String name, String location, String email, String address, String phone, String website) {
//
//        Supplier supplier = new Supplier(UUID.randomUUID(), name, location, email, address, phone, website, LocalDateTime);
//        supplier = repository.saveAndFlush(supplier);
//
//        return supplier;
//
//    }


    private SupplierRequest createSupplierRequest(String name, String email, String address, String phone, String website) {

        return new SupplierRequest(name, email, address, phone, website);

    }


    private Stream<Arguments> dataBlank() {

        return Stream.of(
                Arguments.of("", "leys@mail.ru","","",""),
                Arguments.of("Тихий берег", "","","",""),
                Arguments.of("", "","","","")
        );

    }

//    public Stream<Arguments> invalidData() {
//    }
}

