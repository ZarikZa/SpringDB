package com.example.project2.repository;

import com.example.project2.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; // ✅ Правильный импорт
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Page<Category> findByIsDeletedFalse(Pageable pageable);

    List<Category> findByIsDeletedFalse();

    Page<Category> findByNameContainingIgnoreCaseAndIsDeletedFalse(String name, Pageable pageable);

    @Query("SELECT c FROM Category c WHERE c.isDeleted = false ORDER BY c.name")
    List<Category> findAllActiveForDropdown();

    long countByIsDeletedFalse();
}
