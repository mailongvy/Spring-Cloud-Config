package com.example.StockService.dto;


import lombok.Data;


@Data
public class StockDTO {
    private Long id;
    private Long productId;
    private int quantity;

}
