package com.example.store.service;

import com.example.store.dto.StoreResponseDto;
import com.example.store.entity.Store;
import com.example.store.mapper.StoreMapper;
import com.example.store.repository.StoreRepository;
import com.example.store.request.StoreRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Validated
@Transactional(rollbackOn = Exception.class)
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreMapper mapper;

    public StoreResponseDto createStore(@Valid StoreRequest request) {

        Store store = new Store(UUID.randomUUID(), request.getName(), request.getLocation(), null, request.getEmail());

        storeRepository.saveAndFlush(store);

        return mapper.mapToStoreResponseDto(store);

    }

    public void deleteStore(UUID id) {

        storeRepository.deleteById(id);

    }

    public StoreResponseDto findById(UUID id) {

        Store store = storeRepository.findById(id)
                .orElseThrow();

        return mapper.mapToStoreResponseDto(store);

    }

    public StoreResponseDto updateById(UUID id, @Valid StoreRequest request) {

        Store store = storeRepository.findById(id)
                .orElseThrow();

        store.setName(request.getName());
        store.setLocation(request.getLocation());
        store.setEmail(request.getEmail());

        storeRepository.saveAndFlush(store);

        return mapper.mapToStoreResponseDto(store);

    }

    public StoreResponseDto updateStore(UUID id, @Valid StoreRequest request) {

        Store store = storeRepository.findById(id)
                .orElseThrow();

        store.setName(request.getName());
        store.setLocation(request.getLocation());
        store.setEmail(request.getEmail());

        storeRepository.saveAndFlush(store);

        return mapper.mapToStoreResponseDto(store);

    }

}
