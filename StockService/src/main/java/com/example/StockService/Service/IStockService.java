package com.example.StockService.Service;

import java.util.List;

import com.example.StockService.Model.Stock;
import com.example.StockService.dto.StockDTO;

public interface IStockService {
    StockDTO getStockByProductId(Long productId);

    void reduceStock(Long productId, int quantity);

    Stock addStock(Stock stock);

    List<Stock> getAllStocks();

    // hàm này tránh trường hợp bị lỗi 
    void increaseStock(Long productId, int quantity);

    
}
