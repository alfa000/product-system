package com.adnan.test_interview_wti.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderProductResponse {

    private Integer id;

    private String name;

    private String type;

    private Double price;

    private Integer qty;

    private Double total;
}
