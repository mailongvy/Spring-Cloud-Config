package com.example.StockService.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private int quantity;

    //Optimistic locking
    // mỗi bản ghi stock có 1 version
    // Nếu hai giao dịch (T1, T2) đồng thời cập nhật, T1 lưu thành công và tăng version. T2 sẽ thất bại (ném OptimisticLockException) vì version không khớp, buộc T2 retry hoặc thất bại.
    // => tránh trường hợp bị race condition
    @Version
    private Long version; 

    public Stock(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
        this.version = 1L; // Khởi tạo version cho optimistic locking
    }

    @PrePersist
    public void prePersist() {
        if (this.version == null) {
            this.version = 1L;
        }
    }


    
}
