package com.example.project2.repository;

import com.example.project2.model.Credentials;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CredentialsRepository extends JpaRepository<Credentials, Long> {
    Optional<Credentials> findByUsernameAndIsLockedFalse(String username);

    Page<Credentials> findByUsernameContainingIgnoreCaseAndIsLockedFalse(String username, Pageable pageable);

    @Query("SELECT c FROM Credentials c WHERE c.isLocked = false ORDER BY c.username")
    List<Credentials> findAllActiveForDropdown();
}