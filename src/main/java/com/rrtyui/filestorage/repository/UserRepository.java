package com.rrtyui.filestorage.repository;

import com.rrtyui.filestorage.entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByUsername(String username);

    boolean existsByUsername(String  username);
}
