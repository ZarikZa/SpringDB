package com.example.project2.repository;

import com.example.project2.model.User;
import com.example.project2.model.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByIsDeletedFalseAndIsActiveTrue(Pageable pageable);


    Page<User> findByRoleAndIsDeletedFalseAndIsActiveTrue(UserRole role, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.isDeleted = false AND u.isActive = true ORDER BY u.firstName, u.lastName")
    List<User> findAllActiveForDropdown();
    Page<User> findByEmailContainingIgnoreCaseAndIsDeletedFalseAndIsActiveTrue(String email, Pageable pageable);
    Page<User> findByFirstNameContainingIgnoreCaseAndIsDeletedFalseAndIsActiveTrue(String firstName, Pageable pageable);

    Page<User> findByLastNameContainingIgnoreCaseAndIsDeletedFalseAndIsActiveTrue(String lastName, Pageable pageable);

    @Query("SELECT u FROM User u WHERE " +
            "(LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
            "u.isDeleted = false AND u.isActive = true")

    Page<User> findByEmailOrFirstNameOrLastNameContainingIgnoreCase(
            @Param("searchTerm") String searchTerm, Pageable pageable);

}