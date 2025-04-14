package com.rrtyui.filestorage.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionTemplate;

import static org.junit.jupiter.api.Assertions.*;


@RequiredArgsConstructor
class UserRepositoryTest {
    private final EntityManager entityManager;
    private final TransactionTemplate transactionTemplate;
    private final UserRepository userRepository;

    @Test
    void delete() {

    }
}