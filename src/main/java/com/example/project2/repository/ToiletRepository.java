package com.example.project2.repository;

import com.example.project2.model.Toilet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToiletRepository extends JpaRepository<Toilet, Long> {
    Page<Toilet> findByIsActiveTrue(Pageable pageable);

    Page<Toilet> findByBrandContainingIgnoreCaseAndIsActiveTrue(String brand, Pageable pageable);
    Page<Toilet> findByModelContainingIgnoreCaseAndIsActiveTrue(String model, Pageable pageable);
    Page<Toilet> findByCategoryIdAndIsActiveTrue(Long categoryId, Pageable pageable);

    Page<Toilet> findByWaterSavingAndIsActiveTrue(boolean waterSaving, Pageable pageable);

    @Query("SELECT t FROM Toilet t WHERE t.isActive = true ORDER BY t.name")
    List<Toilet> findAllActiveForDropdown();
}