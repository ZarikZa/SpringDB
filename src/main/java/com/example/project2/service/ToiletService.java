package com.example.project2.service;

import com.example.project2.model.Toilet;
import com.example.project2.model.Category;
import com.example.project2.model.Order;
import com.example.project2.model.OrderItem;
import java.util.List;

public interface ToiletService {
    List<Toilet> findAllToilets();
    List<Toilet> findActiveToilets();
    Toilet findToiletById(Long id);
    Toilet addToilet(Toilet toilet);
    Toilet updateToilet(Toilet toilet);
    void deleteToilet(Long id);
    boolean softDeleteToilet(Long id);
    void deleteMultipleToilets(List<Long> ids);
    void softDeleteMultipleToilets(List<Long> ids);
    List<Toilet> searchToilets(String searchTerm);
    List<Toilet> filterToilets(String brand, Double maxPrice, Long categoryId);
    List<Toilet> getToiletsWithPagination(int page, int size, List<Toilet> sourceList);
    long getTotalToiletsCount(List<Toilet> sourceList);
    List<String> getAllBrands();
    List<String> getAllMaterials();
    List<Category> getAllCategories();
    Category findCategoryById(Long id);
    String getCategoryNameById(Long categoryId);

    List<Toilet> findByIds(List<Long> ids);
    List<OrderItem> createOrderItemsForToilets(List<Toilet> toilets, Order order);
}
