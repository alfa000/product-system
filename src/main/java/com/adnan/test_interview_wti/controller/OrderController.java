package com.adnan.test_interview_wti.controller;

import com.adnan.test_interview_wti.model.dto.OrderRequest;
import com.adnan.test_interview_wti.model.dto.OrderResponse;
import com.adnan.test_interview_wti.model.dto.WebResponse;
import com.adnan.test_interview_wti.model.entity.User;
import com.adnan.test_interview_wti.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping("/order")
    public WebResponse<OrderResponse> order(User user, @RequestBody OrderRequest request) {
        OrderResponse orderResponse = orderService.store(user, request);
        return WebResponse.<OrderResponse>builder()
                .message("Data Created")
                .data(orderResponse)
                .build();
    }
}
