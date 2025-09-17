package com.example.service;

import com.example.dto.AllStoresResponseDto;
import com.example.dto.ProductResponseDto;
import com.example.dto.StoreResponseDto;
import com.example.entity.Product;
import com.example.entity.Store;
import com.example.entity.StoreProduct;
import com.example.mapper.StoreMapper;
import com.example.repository.ProductRepository;
import com.example.repository.StoreProductRepository;
import com.example.repository.StoreRepository;
import com.example.request.ProductRequest;
import com.example.request.StoreRequest;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Validated
@Transactional(readOnly = true)
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreProductRepository storeProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreMapper mapper;
    @Autowired
    private StoreMapper storeMapper;

    @Transactional(rollbackFor = Exception.class)
    public StoreResponseDto createStore(@Valid StoreRequest request) {

        Store store = new Store(UUID.randomUUID(), request.getName(), request.getLocation(), null, request.getEmail());

        storeRepository.saveAndFlush(store);

        return mapper.mapToStoreResponseDto(store);

    }

    @Transactional(rollbackFor = Exception.class)
    public ProductResponseDto createProduct(UUID storeId, @Valid ProductRequest request) {

        Product product = new Product(UUID.randomUUID(), request.getName(), request.getPrice(), "some");
        StoreProduct storeProduct = new StoreProduct(UUID.randomUUID(), storeId, product.getId());

        productRepository.saveAndFlush(product);
        storeProductRepository.saveAndFlush(storeProduct);

        return mapper.mapToProductResponseDto(product);

    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteStore(UUID id) {

        storeRepository.deleteById(id);

    }

    public StoreResponseDto findById(UUID id) {

        Store store = storeRepository.findById(id)
                .orElseThrow();

        return mapper.mapToStoreResponseDto(store);

    }

    @Transactional(rollbackFor = Exception.class)
    public StoreResponseDto updateStore(UUID id, @Valid StoreRequest request) {

        Store store = storeRepository.findById(id)
                .orElseThrow();

        store.setName(request.getName());
        store.setLocation(request.getLocation());
        store.setEmail(request.getEmail());

        storeRepository.saveAndFlush(store);

        return mapper.mapToStoreResponseDto(store);

    }

    public List<AllStoresResponseDto> findAllStores() {

        List<Store> stores = storeRepository.findAll();

        List<AllStoresResponseDto> list = stores.stream()
                .map(store -> mapper.mapToAllStoresResponseDto(store))
                .toList();

        return list;

    }

    public List<AllStoresResponseDto> findAllStoresByLocation(String location) {

        List<Store> stores = storeRepository.findByLocation(location);

        List<AllStoresResponseDto> listLocation = stores.stream()
                .map(store -> mapper.mapToAllStoresResponseDto(store))
                .toList();

        return listLocation;

    }

    public List<AllStoresResponseDto> findAllStoresByName() {

        List<Store> stores = storeRepository.findAll(Sort.by(Sort.Order.asc("name")));

        return stores.stream()
                .map(mapper::mapToAllStoresResponseDto)
                .toList();

    }

    @Transactional(rollbackFor = Exception.class)
    public StoreResponseDto copy(UUID storeId) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow();

        Store copyStore = new Store(UUID.randomUUID(), store.getName(), store.getLocation(), store.getUpdatedAt(), store.getEmail());

        storeRepository.saveAndFlush(copyStore);

        return mapper.mapToStoreResponseDto(store);

    }

    public List<ProductResponseDto> findAllProductByLocation(String street) {

        //Шаг 1 получаем все магазины
        List<Store> allStores = storeRepository.findAll();
        //Шаг 2 фильтруем магазины по указанной улице
        List<Store> storesOnStreet = new ArrayList<>();

        for (Store store : allStores) {

            if (store.getLocation().contains(street) && store.getLocation() != null) {
                storesOnStreet.add(store);
            }

        }

        List<ProductResponseDto> result = new ArrayList<>();

        //Шаг 3 собираем все товары из найденных магазинов
        for (Store store : storesOnStreet) {
            List<StoreProduct> storeProduct = storeProductRepository.findByStoreId(store.getId());

            for (StoreProduct sp : storeProduct) {
                Product product = productRepository.findById(sp.getProductId())
                        .orElseThrow();

                ProductResponseDto productResponseDto = mapper.mapToProductResponseDto(product);

                result.add(productResponseDto);

            }
        }

        return result.stream()
                .distinct()
                .toList();

    }

    public List<ProductResponseDto> findUniqueProducts() {
        //Шаг1 получаем все товары
        List<Product> allProducts = productRepository.findAll();
        //Шаг 2 получаем результирующий список
        List<ProductResponseDto> result = new ArrayList<>();
        //Шаг 3 проходим по каждому товару, проверяем уникальность
        //Шаг 4 считаем количество магазинов для этого товара, вызываем метод в репозитории, который выполняет наш запрос
        //Шаг 5 если товар есть только в одном магазине, мы его добавляем в результат
        for (Product product : allProducts) {

            int countStore = storeRepository.countStoreByProductId(product.getId());

            if (countStore == 1) {
                result.add(mapper.mapToProductResponseDto(product));

            }

        }

        return result;

    }

}
