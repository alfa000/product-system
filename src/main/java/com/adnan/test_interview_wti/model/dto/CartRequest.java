package com.adnan.test_interview_wti.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartRequest {

    @NotNull
    @JsonProperty("product_id")
    private Integer productId;

    @NotNull
    private Integer qty;
}
