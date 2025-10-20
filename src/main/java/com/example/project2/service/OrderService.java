package com.example.project2.service;


import com.example.project2.model.Order;
import com.example.project2.model.OrderStatus;
import com.example.project2.model.User;

import java.util.List;

public interface OrderService {
    List<Order> findAllOrders();
    List<Order> findActiveOrders();
    Order findOrderById(Long id);
    Order addOrder(Order order);
    Order updateOrder(Order order);
    void deleteOrder(Long id);
    boolean softDeleteOrder(Long id);
    void deleteMultipleOrders(List<Long> ids);
    void softDeleteMultipleOrders(List<Long> ids);
    List<Order> searchOrders(String searchTerm);
    List<Order> filterOrders(OrderStatus status, Long userId);
    List<Order> getOrdersWithPagination(int page, int size, List<Order> sourceList);
    long getTotalOrdersCount(List<Order> sourceList);
    List<User> getAllUsers();

}