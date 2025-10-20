package com.example.project2.service;


import com.example.project2.model.OrderItem;
import com.example.project2.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    @Autowired
    private OrderItemRepository repo;

    @Override
    public List<OrderItem> findAllOrderItems() { return repo.findAll(); }
    @Override
    public OrderItem findOrderItemById(Long id) { return repo.findById(id).orElse(null); }
    @Override
    public OrderItem addOrderItem(OrderItem orderItem) { return repo.save(orderItem); }
    @Override
    public OrderItem updateOrderItem(OrderItem orderItem) { return repo.save(orderItem); }
    @Override
    public void deleteOrderItem(Long id) { repo.deleteById(id); }
    @Override
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) { return repo.findByOrderId(orderId); }
    @Override
    public List<OrderItem> getOrderItemsByToiletId(Long toiletId) { return repo.findByToiletId(toiletId); }
    @Override
    public List<OrderItem> getOrderItemsWithPagination(int page, int size) {
        return repo.findAll(PageRequest.of(page - 1, size)).getContent();
    }
    @Override
    public long getTotalOrderItemsCount() { return repo.count(); }
}