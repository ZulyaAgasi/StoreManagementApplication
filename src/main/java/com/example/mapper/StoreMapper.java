package com.example.mapper;

import com.example.dto.AllStoresResponseDto;
import com.example.dto.StoreResponseDto;
import com.example.entity.Store;
import org.springframework.stereotype.Component;

@Component
public class StoreMapper {

    public StoreResponseDto mapToStoreResponseDto(Store store) {

        return new StoreResponseDto(store.getId(), store.getName(), store.getLocation(), store.getEmail());

    }

    public AllStoresResponseDto mapToAllStoresResponseDto(Store store) {

        return new AllStoresResponseDto(store.getId(), store.getName(), store.getLocation());

    }

}
