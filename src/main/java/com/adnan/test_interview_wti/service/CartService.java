package com.adnan.test_interview_wti.service;

import com.adnan.test_interview_wti.model.entity.Cart;
import com.adnan.test_interview_wti.model.entity.Product;
import com.adnan.test_interview_wti.model.entity.User;
import com.adnan.test_interview_wti.model.dto.PagingRequest;
import com.adnan.test_interview_wti.model.dto.CartRequest;
import com.adnan.test_interview_wti.model.dto.CartResponse;
import com.adnan.test_interview_wti.repository.CartRepository;
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
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Validator validator;

    private CartResponse toCartResponse(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .name(cart.getProduct().getName())
                .type(cart.getProduct().getType())
                .price(cart.getProduct().getPrice())
                .qty(cart.getQty())
                .total(cart.getQty()*cart.getProduct().getPrice())
                .build();
    }

    public Page<CartResponse> list(User user, PagingRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Cart> carts = cartRepository.findAll(pageable);
        List<CartResponse> cartResponses = carts.getContent().stream()
                .map(this::toCartResponse)
                .toList();
        return new PageImpl<>(cartResponses, pageable, carts.getTotalElements());
    }

    @Transactional
    public CartResponse store(User user, CartRequest request) {
        Set<ConstraintViolation<CartRequest>> constraintViolations = validator.validate(request);
        if (constraintViolations.size() != 0){
            throw new ConstraintViolationException(constraintViolations);
        }

        Product product = productRepository.findById(request.getProductId().toString())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product not Found"));

        if (product.getStock() < request.getQty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product out of stock");
        }

        product.setStock(product.getStock() - request.getQty());
        productRepository.save(product);

        Cart cart = new Cart();
        cart.setProduct(product);
        cart.setUserId(user.getId());
        cart.setQty(request.getQty());
        cartRepository.save(cart);

        return toCartResponse(cart);
    }

    @Transactional
    public CartResponse update(User user, String id, CartRequest request) {
        Set<ConstraintViolation<CartRequest>> constraintViolations = validator.validate(request);
        if (constraintViolations.size() != 0){
            throw new ConstraintViolationException(constraintViolations);
        }

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not Found"));

        Product product = productRepository.findById(request.getProductId().toString())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if ((product.getStock()+cart.getQty()) < request.getQty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product out of stock");
        }

        product.setStock((product.getStock()+cart.getQty()) - request.getQty());
        productRepository.save(product);

        cart.setProduct(product);
        cart.setUserId(user.getId());
        cart.setQty(request.getQty());
        cartRepository.save(cart);

        return toCartResponse(cart);
    }

    @Transactional
    public void delete(String id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not Found"));

        Product product = productRepository.findById(cart.getProduct().getId().toString())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setStock((product.getStock()+cart.getQty()) - cart.getQty());
        productRepository.save(product);

        cartRepository.delete(cart);
    }
}
