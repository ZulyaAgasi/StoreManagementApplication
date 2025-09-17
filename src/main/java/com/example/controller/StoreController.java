package com.example.controller;

import com.example.dto.AllStoresResponseDto;
import com.example.dto.ProductResponseDto;
import com.example.dto.StoreResponseDto;
import com.example.request.ProductRequest;
import com.example.request.StoreRequest;
import com.example.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/stores")
@Validated
public class StoreController {

    @Autowired
    private StoreService storeService;

    @PostMapping
    public ResponseEntity<StoreResponseDto> createStore(@Valid @RequestBody StoreRequest request) {

        StoreResponseDto storeResponseDto = storeService.createStore(request);

        return ResponseEntity.ok(storeResponseDto);

    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreResponseDto> findStoreById(@PathVariable UUID id) {

        StoreResponseDto store = storeService.findById(id);

        return ResponseEntity.ok(store);

    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreResponseDto> updateStore(@PathVariable UUID id, @Valid @RequestBody StoreRequest request) {

        StoreResponseDto updatedStore = storeService.updateStore(id, request);

        return ResponseEntity.ok(updatedStore);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable UUID id) {

        storeService.deleteStore(id);

        return ResponseEntity.noContent().build();

    }

    @GetMapping
    public ResponseEntity<List<AllStoresResponseDto>> findAllStores() {

        List<AllStoresResponseDto> allStoresResponses = storeService.findAllStores();

        return ResponseEntity.ok(allStoresResponses);

    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<AllStoresResponseDto>> findStoreByLocation(@PathVariable String location) {

        List<AllStoresResponseDto> allStoresByLocationResponses = storeService.findAllStoresByLocation(location);

        return ResponseEntity.ok(allStoresByLocationResponses);

    }

    @GetMapping("/sorted")
    public ResponseEntity<List<AllStoresResponseDto>> findAllStoresByName() {

        List<AllStoresResponseDto> allStoresByNameResponseDto = storeService.findAllStoresByName();

        return ResponseEntity.ok(allStoresByNameResponseDto);

    }

    @PostMapping("/{id}/copy")
    public ResponseEntity<StoreResponseDto> copyStore(@PathVariable UUID id) {

        StoreResponseDto copyStoreResponseDto = storeService.copy(id);

        return ResponseEntity.ok(copyStoreResponseDto);

    }

    @GetMapping("/product/by-location")
    @Operation(summary = "найти товары в магазинах на улице ",
            description = "все товары в магазине на указанной улице ")
    public ResponseEntity<List<ProductResponseDto>> findAllStoreByLocation(
            @Parameter(description = "название улицы ") @RequestParam String location) {

        List<ProductResponseDto> allProduct = storeService.findAllProductByLocation(location);

        return ResponseEntity.ok(allProduct);
    }

    @GetMapping("/products/unique")
    @Operation(summary = "найти уникальные товары", description = " товары, которые продаются только в одном магазине")
    public ResponseEntity<List<ProductResponseDto>> findUniqueProducts() {

        return ResponseEntity.ok(storeService.findUniqueProducts());

    }

    @PostMapping("/product/{storeId}")
    public ResponseEntity<ProductResponseDto> createProduct(@PathVariable UUID storeId, @RequestBody ProductRequest request){

        return ResponseEntity.ok(storeService.createProduct(storeId, request));

    }

}
