package com.adnan.test_interview_wti.repository;

import com.adnan.test_interview_wti.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    public User findByEmail(String email);
    Optional<User> findFirstByToken(String Token);
}
