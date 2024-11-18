package com.adnan.test_interview_wti.controller;

import com.adnan.test_interview_wti.model.dto.*;
import com.adnan.test_interview_wti.model.entity.User;
import com.adnan.test_interview_wti.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/product")
    public WebResponse<List<ProductResponse>> list(User user,
                                                   @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                   @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {

        PagingRequest request = PagingRequest.builder().page(page).size(size).build();

        Page<ProductResponse> productResponses = productService.list(request);
        return WebResponse.<List<ProductResponse>>builder()
                .message("Data Products")
                .data(productResponses.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(productResponses.getNumber())
                        .totalPage(productResponses.getTotalPages())
                        .size(productResponses.getSize())
                        .build())
                .build();
    }

    @PostMapping("/product")
    public WebResponse<ProductResponse> store(User user, @RequestBody ProductRequest request) {
        ProductResponse productResponses = productService.store(request);
        return WebResponse.<ProductResponse>builder()
                .message("Data Created")
                .data(productResponses)
                .build();
    }

    @PutMapping("/product/{id}")
    public WebResponse<ProductResponse> update(User user, @RequestBody ProductRequest request, @PathVariable("id") String id) {
        ProductResponse productResponses = productService.update(id, request);
        return WebResponse.<ProductResponse>builder()
                .message("Data Updated")
                .data(productResponses)
                .build();
    }

    @DeleteMapping("/product/{id}")
    public WebResponse<String> delete(User user, @PathVariable("id") String id) {
        productService.delete(id);
        return WebResponse.<String>builder().data("OK").build();
    }
}
