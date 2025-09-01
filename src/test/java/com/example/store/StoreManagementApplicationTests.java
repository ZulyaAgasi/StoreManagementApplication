package com.example.store;

import com.example.store.dto.StoreResponseDto;
import com.example.store.repository.StoreRepository;
import com.example.store.request.StoreRequest;
import com.example.store.service.StoreService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

@SpringBootTest
@Transactional
class StoreManagementApplicationTests extends TestContainerInitialization {

	@Autowired
	private StoreRepository repository;

	@Autowired
	private StoreService service;

	@AfterEach
	void clear() {
		repository = null;
	}

//	@Test
//	void createStore_whenNameIsBlank_thenThrow() {
//
//		StoreRequest storeRequest = createStoreRequest("", "");
//
//		Assertions.assertThrows(DataIntegrityViolationException.class, ()-> service.createStore(storeRequest));
//
//	}

	@Test
	void create_whenNameAndLocationNotBlank_thenCreate() {

		StoreRequest storeRequest = createStoreRequest("Пятёрочка", "ул. Урванцева");

		StoreResponseDto storeResponseDto = Assertions.assertDoesNotThrow(() -> service.createStore(storeRequest));

		Assertions.assertEquals(storeRequest.getName(), storeResponseDto.getName());

	}

	private StoreRequest createStoreRequest(String name, String location) {
		return new StoreRequest(name, location);
	}

}
