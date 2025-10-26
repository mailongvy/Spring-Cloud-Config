package com.example.StockService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.StockService.Model.Stock;

import jakarta.persistence.LockModeType;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    // Standard optimistic locking - chỉ check version khi có modification
    // @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Stock findByProductId(Long productId);
    

    // Pessimistic locking - lock ngay lập tức
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Stock s WHERE s.productId = :productId")
    Stock findByProductIdForUpdate(Long productId);

    // Standard optimistic locking - chỉ check version khi có modification
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query("SELECT s FROM Stock s WHERE s.productId = :productId")
    Stock findByProductIdWithOpLock(Long productId);

    

    
}
