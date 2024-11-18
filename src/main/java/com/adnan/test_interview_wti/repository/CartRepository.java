package com.adnan.test_interview_wti.repository;

import com.adnan.test_interview_wti.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String>, JpaSpecificationExecutor<Cart> {
    Optional<Cart> findByIdAndUserId(Integer id, Integer userId);
}
