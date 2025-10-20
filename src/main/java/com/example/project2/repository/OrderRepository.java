package com.example.project2.repository;

import com.example.project2.model.Order;
import com.example.project2.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByIsDeletedFalse(Pageable pageable); // <-- исправлено


    Page<Order> findByStatusAndUserIdAndIsDeletedFalse(OrderStatus status, Long userId, Pageable pageable);

    Page<Order> findByShippingAddressContainingIgnoreCaseAndIsDeletedFalse(String address, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.isDeleted = false AND o.user.id = :userId ORDER BY o.orderDate")
    List<Order> findAllActiveByUserIdForDropdown(Long userId);
    Page<Order> findByStatusAndIsDeletedFalse(OrderStatus status, Pageable pageable);
    Page<Order> findByUserIdAndIsDeletedFalse(Long userId, Pageable pageable);


}