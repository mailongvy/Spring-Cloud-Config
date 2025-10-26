package com.example.StockService.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.StockService.Model.Stock;
import com.example.StockService.Response.ApiResponse;
import com.example.StockService.Service.StockService;
import com.example.StockService.dto.StockDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stock")
public class StockController {
    private final StockService stockService;

    @GetMapping("/get/{productId}")
    public ResponseEntity<ApiResponse> getStockByProductId(@PathVariable Long productId) {
        try {
            StockDTO stockDTO = stockService.getStockByProductId(productId);
            return ResponseEntity.ok(new ApiResponse("Stock found", stockDTO));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                    .body(new ApiResponse("Stock not found", null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addStock(@RequestBody Stock stock) {
        try {
            Stock stock1 = stockService.addStock(stock);
            return ResponseEntity.ok(new ApiResponse("Add successfully", stock1));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                    .body(new ApiResponse(e.getMessage(), null));   
        }
    }    
    
    @PutMapping("/reduce/{productId}")
    public ResponseEntity<ApiResponse> reduceStock(@PathVariable Long productId, @RequestParam int quantity) {
        try {
            stockService.reduceStock(productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Stock reduced successfully", null));
        } catch (org.springframework.dao.OptimisticLockingFailureException e) {
            // Handle optimistic locking conflicts - Race condition detected
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(new ApiResponse("🚨 RACE CONDITION DETECTED! Stock đã bị cập nhật bởi giao dịch khác. Vui lòng thử lại.", null));
        } catch (org.springframework.transaction.TransactionSystemException e) {
            // Handle transaction system exceptions (also related to optimistic locking)
            if (e.getCause() != null && e.getCause().getCause() instanceof jakarta.persistence.OptimisticLockException) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                    .body(new ApiResponse("⚡ OPTIMISTIC LOCKING CONFLICT! Dữ liệu đã thay đổi trong khi bạn xử lý. Hãy refresh và thử lại.", null));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ApiResponse("❌ Lỗi hệ thống giao dịch: " + e.getMessage(), null));
        } catch (RuntimeException e) {
            // Handle specific business logic errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) { 
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ApiResponse("❌ Lỗi không xác định: " + e.getMessage(), null));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse> getAllStocks() {
        try {
            List<Stock> list = stockService.getAllStocks();
            return ResponseEntity.ok(new ApiResponse("Found", list));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                    .body(new ApiResponse("Not Found", null));
        }
    }

    @PutMapping("/increase/{productId}")
    public ResponseEntity<ApiResponse> increaseStock(@PathVariable Long productId, @RequestParam int quantity) {
        try {
            stockService.increaseStock(productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Stock increased successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ApiResponse("Internal server error: " + e.getMessage(), null));
        }
    }

    

}
