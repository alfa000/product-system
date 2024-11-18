package com.adnan.test_interview_wti.service;

import com.adnan.test_interview_wti.model.dto.OrderRequest;
import com.adnan.test_interview_wti.model.dto.OrderResponse;
import com.adnan.test_interview_wti.model.dto.PagingRequest;
import com.adnan.test_interview_wti.model.entity.Cart;
import com.adnan.test_interview_wti.model.entity.Order;
import com.adnan.test_interview_wti.model.entity.OrderProducts;
import com.adnan.test_interview_wti.model.entity.User;
import com.adnan.test_interview_wti.repository.CartRepository;
import com.adnan.test_interview_wti.repository.OrderProductRepository;
import com.adnan.test_interview_wti.repository.OrderRepository;
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

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private Validator validator;

    private OrderResponse toOrderResponse(Order order) {
//        List<OrderProducts> orderProductsList = orderProductRepository.findAllByOrderId(order.getId());
//
//        List<OrderDetailResponse> detailResponses = orderProductsList.stream()
//                .map(detail -> new OrderDetailResponse(
//                        detail.getProduct().getName(),
//                        detail.getPrice(),
//                        detail.getQty(),
//                        detail.getTotalPrice()))
//                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .date(order.getDate())
                .total(order.getTotalPrice())
//                .orderDetail(detailResponses) // ini list detail order
                .build();
    }

    public Page<OrderResponse> get(User user, PagingRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Order> orders = orderRepository.findAll(pageable);
        List<OrderResponse> orderResponses = orders.getContent().stream()
                .map(this::toOrderResponse)
                .toList();
        return new PageImpl<>(orderResponses, pageable, orders.getTotalElements());
    }

    @Transactional
    public OrderResponse store(User user, OrderRequest request) {
        Set<ConstraintViolation<OrderRequest>> constraintViolations = validator.validate(request);
        if (constraintViolations.size() != 0){
            throw new ConstraintViolationException(constraintViolations);
        }

        Integer counter = 0;
        Integer cartReqCount = request.getCartId().size();
        double totalPrice = 0;
        for (Integer cartId : request.getCartId()) {
            Cart cart = cartRepository.findByIdAndUserId(cartId, user.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not Found"));

            totalPrice += cart.getProduct().getPrice()*cart.getQty();
            counter++;
        }

        if (counter.equals(cartReqCount)) {
            Order order = new Order();
            order.setUserId(user.getId());
            order.setDate(new Date());
            order.setTotalPrice((float) totalPrice);
            orderRepository.save(order);

            for (Integer cartId : request.getCartId()) {
                Cart cart = cartRepository.findByIdAndUserId(cartId, user.getId())
                        .orElseThrow(() -> new RuntimeException("Product not found"));

                OrderProducts orderProducts = new OrderProducts();
                orderProducts.setOrderId(order.getId());
                orderProducts.setProductId(cart.getProduct().getId());
                orderProducts.setPrice(cart.getProduct().getPrice().floatValue());
                orderProducts.setQty(cart.getQty());
                orderProducts.setTotalPrice((float) (cart.getProduct().getPrice()*cart.getQty()));
                orderProductRepository.save(orderProducts);
                cartRepository.delete(cart);
            }
            return toOrderResponse(order);
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
    }

}
