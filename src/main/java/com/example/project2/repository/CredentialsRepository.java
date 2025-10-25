package com.example.project2.repository;

import com.example.project2.model.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialsRepository extends JpaRepository<Credentials, Long> {
    Optional<Credentials> findByUsername(String username);
    boolean existsByUsername(String username);

    @Query("SELECT c FROM Credentials c JOIN c.user u WHERE c.username = :username AND u.isActive = true AND u.isDeleted = false")
    Optional<Credentials> findByUsernameAndUserActive(@Param("username") String username);
}