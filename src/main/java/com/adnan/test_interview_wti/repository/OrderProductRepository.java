package com.adnan.test_interview_wti.repository;

import com.adnan.test_interview_wti.model.entity.OrderProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProducts, String> {
    List<OrderProducts> findAllByOrderId(Integer orderId);
}
