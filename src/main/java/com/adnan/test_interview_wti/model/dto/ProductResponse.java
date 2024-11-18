package com.adnan.test_interview_wti.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

    private Integer productId;

    private String name;

    private String type;

    private Double price;

    private Integer stock;
}
