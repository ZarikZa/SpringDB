package com.example.project2.service;


import com.example.project2.model.OrderItem;
import com.example.project2.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OrderItemService {
    List<OrderItem> findAllOrderItems();
    OrderItem findOrderItemById(Long id);
    OrderItem addOrderItem(OrderItem orderItem);
    OrderItem updateOrderItem(OrderItem orderItem);
    void deleteOrderItem(Long id);
    List<OrderItem> getOrderItemsByOrderId(Long orderId);
    List<OrderItem> getOrderItemsByToiletId(Long toiletId);
    List<OrderItem> getOrderItemsWithPagination(int page, int size);
    long getTotalOrderItemsCount();
}
