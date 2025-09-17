package com.example.repository;

import com.example.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {

    List<Store> findByLocation(String location);
// здесь идет подсчет количества магазина для конкретного товара:
// мы обращаемся к таблице store_products фильтруем записи по productId,
// считаем количество найденных записей, получаем количество и дальше мы с ним работаем
    @Query("SELECT COUNT(sp) FROM store_products sp WHERE sp.productId = :id")
    int countStoreByProductId(UUID id);

}
