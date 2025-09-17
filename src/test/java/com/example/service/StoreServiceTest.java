package com.example.service;

import com.example.TestContainerInitialization;
import com.example.dto.ProductResponseDto;
import com.example.dto.StoreResponseDto;
import com.example.entity.Product;
import com.example.entity.Store;
import com.example.entity.StoreProduct;
import com.example.repository.ProductRepository;
import com.example.repository.StoreProductRepository;
import com.example.repository.StoreRepository;
import com.example.request.StoreRequest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootTest
class StoreServiceTest extends TestContainerInitialization {

    public static final String DEFAULT_STORE_LOCATION = "ул. Ленина";
    public static final String DEFAULT_STORE_NAME = "Пятёрочка";
    public static final String DEFAULT_STORE_EMAIL = "pyaterochka@mail.ru";

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreProductRepository storeProductRepository;

    @Autowired
    private StoreService service;

    @AfterEach
    void clear() {
        storeRepository.deleteAll();
    }

    @ParameterizedTest
    @MethodSource("dataBlank")
    void createStore_whenDataBlank_thenThrow(String name, String location, String email) {

        StoreRequest storeRequest = createStoreRequest(name, location, email);

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.createStore(storeRequest));

    }

    @Test
    void createStore_whenNameAndLocationAndEmailNotBlank_thenCreate() {

        StoreRequest storeRequest = createStoreRequest(DEFAULT_STORE_NAME, DEFAULT_STORE_LOCATION, DEFAULT_STORE_EMAIL);

        StoreResponseDto storeResponseDto = Assertions.assertDoesNotThrow(() -> service.createStore(storeRequest));

        Assertions.assertEquals(storeRequest.getName(), storeResponseDto.getName());

    }

    @ParameterizedTest
    @MethodSource("invalidData")
    void updateStore_whenRequestInvalid_thenThrow(String name, String location, String email) {

        Store store = createStore("монетка", "Ленина", "monetka@mail.ru");

        StoreRequest storeRequest = createStoreRequest(name, location, email);

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.updateStore(store.getId(), storeRequest));

    }

    @Test
    void updateStore_whenStoreNotFoundById_thenThrow() {

        createStore("Пятёрочка", "Ленина", "pyaterochka@mail.ru");

        StoreRequest storeRequest = createStoreRequest("монетка", "Ленина", "monetka@mail.ru");

        Assertions.assertThrows(NoSuchElementException.class, () -> service.updateStore(UUID.randomUUID(), storeRequest));

    }

    @Test
    void updateStore_whenStoreExist_thenUpdate() {

        Store store = createStore("Пятёрочка", "Ленина", "pyaterochka@mail.ru");

        StoreRequest storeRequest = createStoreRequest("монетка", "Ленина", "monetka@mail.ru");

        Assertions.assertDoesNotThrow(() -> service.updateStore(store.getId(), storeRequest));

    }

    @Test
    void deleteStore_whenStoreNotBlank_thenDelete() {

        Store store = createStore("Пятёрочка", "Ленина", "pyaterochka@mail.ru");

        Assertions.assertDoesNotThrow(() -> service.deleteStore(store.getId()));

    }

    @Test
    void deleteStore_whenIdInvalid_thenThrow() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> service.deleteStore(UUID.fromString("6453")));

    }

    @Test
    void findById_whenIdValid_thenFind() {

        Store store = createStore("Пятёрочка", "Ленина", "pyaterochka@mail.ru");

        Assertions.assertDoesNotThrow(() -> service.findById(store.getId()));

    }

    @Test
    void findById_whenIdInvalid_thenThrow() {

        Assertions.assertThrows(NoSuchElementException.class, () -> service.findById(UUID.randomUUID()));

    }

    @Test
    void findAllProductByLocation_whenStoreExistsButStreetIsNull_thenEmptyList() {

        createProduct("молоко", BigDecimal.valueOf(80), "молочка");
        createProduct("пиво", BigDecimal.valueOf(50), "алкоголь");
        createProduct("хлеб", BigDecimal.valueOf(80), "хлебобулочные");

        List<ProductResponseDto> result = Assertions.assertDoesNotThrow(() -> service.findAllProductByLocation("ул. Вязова "));

        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    void findAllProductByLocation_whenStoreExistsButStreetIsNull_thenThrow() {

        Store store = createStore("Пятёрочка", "Ленина", "pyaterochka@mail.ru");

        Product product1 = createProduct("молоко", BigDecimal.valueOf(80), "молочка");
        Product product2 = createProduct("пиво", BigDecimal.valueOf(50), "алкоголь");
        Product product3 = createProduct("хлеб", BigDecimal.valueOf(80), "хлебобулочные");

        createStoreProduct(store.getId(), product1.getId());
        createStoreProduct(store.getId(), product2.getId());
        createStoreProduct(store.getId(), product3.getId());

        Assertions.assertThrows(NullPointerException.class, () -> service.findAllProductByLocation(null));

    }

    @Test
    void findAllProductByLocation_whenStoreNotFoundByStreet_thenReturnEmptyList() {

        Store store = createStore("Пятёрочка", "Ленина", "pyaterochka@mail.ru");

        Product product1 = createProduct("молоко", BigDecimal.valueOf(80), "молочка");
        Product product2 = createProduct("пиво", BigDecimal.valueOf(50), "алкоголь");
        Product product3 = createProduct("хлеб", BigDecimal.valueOf(80), "хлебобулочные");

        createStoreProduct(store.getId(), product1.getId());
        createStoreProduct(store.getId(), product2.getId());
        createStoreProduct(store.getId(), product3.getId());

        List<ProductResponseDto> result = Assertions.assertDoesNotThrow(() ->
                service.findAllProductByLocation("ул. Вязова"));

        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    void findAllProductByLocation_whenStoreProductIsEmpty_thenReturnEmptyList() {

        createStore("Пятёрочка", "Ленина", "pyaterochka@mail.ru");

        createProduct("молоко", BigDecimal.valueOf(80), "молочка");
        createProduct("пиво", BigDecimal.valueOf(50), "алкоголь");
        createProduct("хлеб", BigDecimal.valueOf(80), "хлебобулочные");

        List<ProductResponseDto> result =
                Assertions.assertDoesNotThrow(() -> service.findAllProductByLocation("ул. Ленина"));

        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    void findAllProductByLocation_whenStoreExistButNoProduct_thenReturnEmptyList() {

        Store store = createStore("Пятёрочка", "ул. Вязова", "pyaterochka@mail.ru");
        Store store2 = createStore("Пятёрочка", "ул. Ленина", "pyaterochka@mail.ru");

        Product product1 = createProduct("молоко", BigDecimal.valueOf(80), "молочка");
        Product product2 = createProduct("пиво", BigDecimal.valueOf(50), "алкоголь");
        Product product3 = createProduct("хлеб", BigDecimal.valueOf(80), "хлебобулочные");

        createStoreProduct(store.getId(), product1.getId());
        createStoreProduct(store.getId(), product2.getId());
        createStoreProduct(store.getId(), product3.getId());

        List<ProductResponseDto> result = Assertions.assertDoesNotThrow(() ->
                service.findAllProductByLocation("ул. Ленина"));

        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    void findAllProductByLocation_whenStoreExistAndProductExists_thenReturnProductList() {

        Store store = createStore("Пятёрочка", "ул. Ленина", "pyaterochka@mail.ru");
        Store store2 = createStore("Ярче!", "ул. Ленина", "yarche@mail.ru");

        Product product1 = createProduct("молоко", BigDecimal.valueOf(80), "молочка");
        Product product2 = createProduct("пиво", BigDecimal.valueOf(50), "алкоголь");
        Product product3 = createProduct("хлеб", BigDecimal.valueOf(80), "хлебобулочные");
        Product product4 = createProduct("сыр", BigDecimal.valueOf(120), "молочка");


        createStoreProduct(store.getId(), product1.getId());
        createStoreProduct(store.getId(), product2.getId());
        createStoreProduct(store.getId(), product3.getId());
        createStoreProduct(store2.getId(), product3.getId());
        createStoreProduct(store2.getId(), product4.getId());

        List<ProductResponseDto> result = Assertions.assertDoesNotThrow(() ->
                service.findAllProductByLocation("ул. Ленина"));

        Assertions.assertEquals(4, result.size());
        Assertions.assertEquals(product1.getId(), result.get(0).getId());

    }

    @Test
    void findUniqueProducts_whenUniqueProductsExists_thenReturnUniqueProductList() {

        Store store = createStore("Пятёрочка", "ул. Ленина", "pyaterochka@mail.ru");
        Store store2 = createStore("Ярче!", "ул. Ленина", "yarche@mail.ru");

        Product product1 = createProduct("молоко", BigDecimal.valueOf(80), "молочка");
        Product product2 = createProduct("пиво", BigDecimal.valueOf(50), "алкоголь");
        Product product3 = createProduct("хлеб", BigDecimal.valueOf(80), "хлебобулочные");


        createStoreProduct(store.getId(), product1.getId());
        createStoreProduct(store.getId(), product2.getId());
        createStoreProduct(store.getId(), product3.getId());
        createStoreProduct(store2.getId(), product2.getId());
        createStoreProduct(store2.getId(), product3.getId());

        List<ProductResponseDto> result = Assertions.assertDoesNotThrow(() -> service.findUniqueProducts());

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(product1.getName(), result.get(0).getName());

    }

    private Store createStore(String name, String location, String email) {

        Store store = new Store(UUID.randomUUID(), name, location, null, null);
        store = storeRepository.saveAndFlush(store);

        return store;

    }

    private Product createProduct(String name, BigDecimal price, String category) {

        Product product = new Product(UUID.randomUUID(), name, price, category);
        product = productRepository.saveAndFlush(product);

        return product;

    }

    private StoreProduct createStoreProduct(UUID storeId, UUID productId) {

        StoreProduct storeProduct = new StoreProduct(UUID.randomUUID(), storeId, productId);

        storeProduct = storeProductRepository.saveAndFlush(storeProduct);

        return storeProduct;

    }

    private StoreRequest createStoreRequest(String name, String location, String email) {

        return new StoreRequest(name, location, email);

    }

    private Stream<Arguments> invalidData() {

        return Stream.of(
                Arguments.of("", "ул.Московская", ""),
                Arguments.of("мария-ра", "", ""),
                Arguments.of("", "", "")
        );

    }

    private Stream<Arguments> dataBlank() {

        return Stream.of(
                Arguments.of("", "леонова", "noname@mail.ru"),
                Arguments.of("ярче", "", "yarche@mail.ru"),
                Arguments.of("пятерочка", "московская", ""),
                Arguments.of("мария-ра", "", ""),
                Arguments.of("", "марковцева", ""),
                Arguments.of("", "", "mariya-ra@mail@ru"),
                Arguments.of("", "", "")
        );

    }

}
