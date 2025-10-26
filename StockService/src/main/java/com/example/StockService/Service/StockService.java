package com.example.StockService.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.StockService.Exception.ResourceNotFound;
import com.example.StockService.Model.Stock;
import com.example.StockService.Repository.ProductServiceClient;
import com.example.StockService.Repository.StockRepository;
import com.example.StockService.dto.ApiResponse;
import com.example.StockService.dto.StockDTO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockService implements IStockService {
    private final StockRepository stockRepository;
    private final ProductServiceClient productServiceClient;

    @Override
    public StockDTO getStockByProductId(Long productId) {
        // TODO Auto-generated method stub
        ApiResponse response = productServiceClient.getProductById(productId);
        if (response.getData() == null) {
            throw new ResourceNotFound("Product not found");
        }

        Stock stock = stockRepository.findByProductId(productId);
        if (stock == null) {
            throw new ResourceNotFound("Stock not found");
        }


        StockDTO stockDTO = new StockDTO();
        stockDTO.setId(stock.getId());
        stockDTO.setProductId(productId);
        stockDTO.setQuantity(stock.getQuantity());

        return stockDTO;
    }

    @Override
    @Transactional
    public void reduceStock(Long productId, int quantity) {
        try {
            // tìm stock qua productId
            Stock stock = stockRepository.findByProductIdWithOpLock(productId);
            if (stock == null) {
                throw new RuntimeException("Stock not found");
            }
            if (stock.getQuantity() < quantity) {
                throw new RuntimeException("Insufficient stock");
            }
            
            stock.setQuantity(stock.getQuantity() - quantity);
            stockRepository.save(stock);
            
        } catch (org.springframework.orm.ObjectOptimisticLockingFailureException e) {
            // Optimistic locking exception từ Spring
            throw new RuntimeException("Stock update conflict - another transaction modified this stock. Please try again.", e);
        } catch (jakarta.persistence.OptimisticLockException e) {
            // JPA optimistic lock exception
            throw new RuntimeException("Stock update conflict - optimistic locking failed. Please try again.", e);
        } catch (Exception e) {
            // Các exception khác
            throw new RuntimeException("Failed to reduce stock: " + e.getMessage(), e);
        }
    }

    @Override
    public Stock addStock(Stock stock) {
        // TODO Auto-generated method stub
        ApiResponse response = productServiceClient.getProductById(stock.getProductId());
        if ( response.getData() == null) {
            throw new ResourceNotFound("Product not found");
        }
        Stock stock1 = new Stock(stock.getProductId(), stock.getQuantity());

        return stockRepository.save(stock1);
    }

    @Override
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    @Override
    @Transactional  // ← THÊM @Transactional cho method có sử dụng @Lock
    public void increaseStock(Long productId, int quantity) {
        // TODO Auto-generated method stub
        // Stock stock = stockRepository.findByProductId(productId);
        Stock stock = stockRepository.findByProductId(productId);
        if (stock == null) {
            throw new RuntimeException("Stock not found");
        }
        
        stock.setQuantity(stock.getQuantity() + quantity);
        stockRepository.save(stock);
    }

    


    
    
    
}
