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

    @Test
    void createStore_whenNameIsBlank_thenThrow() {  // TODO: параметризовать эти два теста

        StoreRequest storeRequest = createStoreRequest("", "леонова", "noname@mail.ru");

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.createStore(storeRequest));

    }

    @Test
    void createStore_whenLocationIsBlank_thenThrow() {

        StoreRequest storeRequest = createStoreRequest("ярче", "", "yarche@mail.ru");

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.createStore(storeRequest));

    }

    @Test
    void create_whenNameAndLocationNotBlank_thenCreate() {

        StoreRequest storeRequest = createStoreRequest("Пятёрочка", "ул. Урванцева","pyaterochka@mail.ru");

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

        createStore("Пятёрочка", "Ленина","pyaterochka@mail.ru" );

        StoreRequest storeRequest = createStoreRequest("монетка", "Ленина", "monetka@mail.ru");

        Assertions.assertThrows(NoSuchElementException.class, () -> service.updateStore(UUID.randomUUID(), storeRequest));

    }

    private Store createStore(String name, String location, String email) {

        Store store = new Store(UUID.randomUUID(), name, location, null, null );
        store = repository.saveAndFlush(store);

        return store;

    }


    private StoreRequest createStoreRequest(String name, String location, String email) {

        return new StoreRequest(name, location, "email");

    }

    private Stream<Arguments> invalidData() {

        return Stream.of(
                Arguments.of("", "ул.Московская",""),
                Arguments.of("мария-ра", "",""),
                Arguments.of("", "","")
        );

    }

}