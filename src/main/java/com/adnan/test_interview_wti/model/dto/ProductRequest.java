package com.adnan.test_interview_wti.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    private String type;

    @NotNull
    private Double price;

    @NotNull
    private Integer stock;
}
