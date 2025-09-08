package com.example.store.service;

import com.example.store.TestContainerInitialization;
import com.example.store.dto.StoreResponseDto;
import com.example.store.entity.Store;
import com.example.store.repository.StoreRepository;
import com.example.store.request.StoreRequest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootTest
class StoreServiceTest extends TestContainerInitialization {

    @Autowired
    private StoreRepository repository;

    @Autowired
    private StoreService service;

    @AfterEach
    void clear() {
        repository.deleteAll();
    }

    @ParameterizedTest
    @MethodSource("dataBlank")
    void createStore_whenDataBlank_thenThrow(String name, String location, String email) {

        StoreRequest storeRequest = createStoreRequest(name, location, email);

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.createStore(storeRequest));

    }

    @Test
    void createStore_whenNameAndLocationAndEmailNotBlank_thenCreate() {

        StoreRequest storeRequest = createStoreRequest("Пятёрочка", "ул. Урванцева", "pyaterochka@mail.ru");

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

    private Store createStore(String name, String location, String email) {

        Store store = new Store(UUID.randomUUID(), name, location, null, null);
        store = repository.saveAndFlush(store);

        return store;

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
