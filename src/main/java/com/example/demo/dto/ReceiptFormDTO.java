package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.Map;

@Data
public class ReceiptFormDTO {
    private LocalDate actualArrival;
    private String receivedNote;
    private Map<Long, Integer> actualQuantities;
}