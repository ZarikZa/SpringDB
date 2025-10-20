package com.example.project2.service;

import com.example.project2.model.Order;
import com.example.project2.model.OrderStatus;
import com.example.project2.model.User;
import com.example.project2.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository repo;

    @Autowired
    private UserService userService;

    @Override
    public List<Order> findAllOrders() {
        return repo.findAll();
    }

    @Override
    public List<Order> findActiveOrders() {
        return repo.findByIsDeletedFalse(Pageable.unpaged()).getContent();
    }

    @Override
    public Order findOrderById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public Order addOrder(Order order) {
        return repo.save(order);
    }

    @Override
    public Order updateOrder(Order order) {
        Order existingOrder = findOrderById(order.getId());
        if (existingOrder != null) {
            existingOrder.setStatus(order.getStatus());
            existingOrder.setShippingAddress(order.getShippingAddress());
            existingOrder.setNotes(order.getNotes());
            existingOrder.setTotalAmount(order.getTotalAmount());

            if (order.getUser() != null) {
                existingOrder.setUser(order.getUser());
            }

            return repo.save(existingOrder);
        }
        return null;
    }

    @Override
    public void deleteOrder(Long id) {
        repo.deleteById(id);
    }

    @Override
    public boolean softDeleteOrder(Long id) {
        Order order = findOrderById(id);
        if (order != null && !order.isDeleted()) {
            order.setDeleted(true);
            repo.save(order);
            return true;
        }
        return false;
    }

    @Override
    public void deleteMultipleOrders(List<Long> ids) {
        repo.deleteAllById(ids);
    }

    @Override
    public void softDeleteMultipleOrders(List<Long> ids) {
        repo.findAllById(ids).forEach(order -> {
            if (!order.isDeleted()) {
                order.setDeleted(true);
                repo.save(order);
            }
        });
    }

    @Override
    public List<Order> searchOrders(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findActiveOrders();
        }
        return repo.findByShippingAddressContainingIgnoreCaseAndIsDeletedFalse(
                searchTerm.trim(), Pageable.unpaged()).getContent();
    }

    @Override
    public List<Order> filterOrders(OrderStatus status, Long userId) {
        if (status != null && userId != null) {
            return repo.findByStatusAndUserIdAndIsDeletedFalse(status, userId, Pageable.unpaged()).getContent();
        } else if (status != null) {
            return repo.findByStatusAndIsDeletedFalse(status, Pageable.unpaged()).getContent();
        } else if (userId != null) {
            return repo.findByUserIdAndIsDeletedFalse(userId, Pageable.unpaged()).getContent();
        } else {
            return findActiveOrders();
        }
    }

    @Override
    public List<Order> getOrdersWithPagination(int page, int size, List<Order> sourceList) {
        if (sourceList == null || sourceList.isEmpty()) {
            return new ArrayList<>();
        }

        int safePage = Math.max(page - 1, 0);
        int start = safePage * size;
        int end = Math.min(start + size, sourceList.size());

        if (start >= sourceList.size()) {
            return new ArrayList<>();
        }

        return sourceList.subList(start, end);
    }

    @Override
    public long getTotalOrdersCount(List<Order> sourceList) {
        return sourceList != null ? sourceList.size() : 0;
    }

    @Override
    public List<User> getAllUsers() {
        return userService.findActiveUsers();
    }
}