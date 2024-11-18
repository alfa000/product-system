package com.adnan.test_interview_wti.controller;

import com.adnan.test_interview_wti.model.dto.*;
import com.adnan.test_interview_wti.model.entity.User;
import com.adnan.test_interview_wti.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping("/cart")
    public WebResponse<List<CartResponse>> list(User user,
                                                @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {

        PagingRequest request = PagingRequest.builder().page(page).size(size).build();

        Page<CartResponse> cartResponses = cartService.list(user, request);
        return WebResponse.<List<CartResponse>>builder()
                .message("Data Carts")
                .data(cartResponses.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(cartResponses.getNumber())
                        .totalPage(cartResponses.getTotalPages())
                        .size(cartResponses.getSize())
                        .build())
                .build();
    }

    @PostMapping("/cart")
    public WebResponse<CartResponse> store(User user, @RequestBody CartRequest request) {
        CartResponse cartResponses = cartService.store(user, request);
        return WebResponse.<CartResponse>builder()
                .message("Data Created")
                .data(cartResponses)
                .build();
    }

    @PutMapping("/cart/{id}")
    public WebResponse<CartResponse> update(User user, @RequestBody CartRequest request, @PathVariable("id") String id) {
        CartResponse cartResponses = cartService.update(user, id, request);
        return WebResponse.<CartResponse>builder()
                .message("Data Updated")
                .data(cartResponses)
                .build();
    }

    @DeleteMapping("/cart/{id}")
    public WebResponse<String> delete(@PathVariable("id") String id) {
        cartService.delete(id);
        return WebResponse.<String>builder().data("OK").build();
    }
}
