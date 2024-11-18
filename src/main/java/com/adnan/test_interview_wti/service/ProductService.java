package com.adnan.test_interview_wti.service;

import com.adnan.test_interview_wti.model.entity.Product;
import com.adnan.test_interview_wti.model.dto.PagingRequest;
import com.adnan.test_interview_wti.model.dto.ProductRequest;
import com.adnan.test_interview_wti.model.dto.ProductResponse;
import com.adnan.test_interview_wti.repository.ProductRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Validator validator;

    private ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .productId(product.getId())
                .name(product.getName())
                .type(product.getType())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }

    public Page<ProductResponse> list(PagingRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Product> products = productRepository.findAll(pageable);
        List<ProductResponse> productResponses = products.getContent().stream()
                .map(this::toProductResponse)
                .toList();
        return new PageImpl<>(productResponses, pageable, products.getTotalElements());
    }

    @Transactional
    public ProductResponse store(ProductRequest request) {
        Set<ConstraintViolation<ProductRequest>> constraintViolations = validator.validate(request);
        if (constraintViolations.size() != 0){
            throw new ConstraintViolationException(constraintViolations);
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setType(request.getType());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        productRepository.save(product);

        return toProductResponse(product);
    }

    @Transactional
    public ProductResponse update(String id, ProductRequest request) {
        Set<ConstraintViolation<ProductRequest>> constraintViolations = validator.validate(request);
        if (constraintViolations.size() != 0){
            throw new ConstraintViolationException(constraintViolations);
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not Found"));

        product.setName(request.getName());
        product.setType(request.getType());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        productRepository.save(product);

        return toProductResponse(product);
    }

    @Transactional
    public void delete(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not Found"));

        productRepository.delete(product);
    }
}
