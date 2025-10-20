package com.example.project2.service;


import com.example.project2.model.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {
    List<Category> findAllCategories();
    List<Category> findActiveCategories();
    Category findCategoryById(Long id);
    Category addCategory(Category category);
    Category updateCategory(Category category);
    void deleteCategory(Long id);
    boolean softDeleteCategory(Long id);
    void deleteMultipleCategories(List<Long> ids);
    void softDeleteMultipleCategories(List<Long> ids);
    List<Category> searchCategories(String searchTerm);
    List<Category> getCategoriesWithPagination(int page, int size, List<Category> sourceList);
    long getTotalCategoriesCount(List<Category> sourceList);
    Page<Category> findActiveCategoriesWithPagination(int page, int size);
    Page<Category> searchCategoriesWithPagination(String searchTerm, int page, int size);
}