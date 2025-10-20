package com.example.project2.service;

import com.example.project2.model.Category;
import com.example.project2.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired private CategoryRepository repo;

    @Override
    public List<Category> findAllCategories() { return repo.findAll(); }
    @Override
    public List<Category> findActiveCategories() { return repo.findByIsDeletedFalse(); }
    @Override
    public Category findCategoryById(Long id) { return repo.findById(id).orElse(null); }
    @Override
    public Category addCategory(Category category) { return repo.save(category); }
    @Override
    public Category updateCategory(Category category) { return repo.save(category); }
    @Override
    public void deleteCategory(Long id) { repo.deleteById(id); }
    @Override
    public boolean softDeleteCategory(Long id) {
        Category category = findCategoryById(id);
        if (category != null && !category.isDeleted()) {
            category.setDeleted(true);
            repo.save(category);
            return true;
        }
        return false;
    }
    @Override
    public void deleteMultipleCategories(List<Long> ids) { repo.deleteAllById(ids); }
    @Override
    public void softDeleteMultipleCategories(List<Long> ids) {
        repo.findAllById(ids).forEach(category -> {
            if (!category.isDeleted()) {
                category.setDeleted(true);
                repo.save(category);
            }
        });
    }
    @Override
    public List<Category> searchCategories(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) return findActiveCategories();
        return repo.findByNameContainingIgnoreCaseAndIsDeletedFalse(searchTerm, Pageable.unpaged()).getContent();
    }
    @Override
    public List<Category> getCategoriesWithPagination(int page, int size, List<Category> sourceList) {
        int safePage = page < 1 ? 0 : page - 1;
        return repo.findByIsDeletedFalse(PageRequest.of(safePage, size)).getContent();
    }

    @Override
    public long getTotalCategoriesCount(List<Category> sourceList) {
        return repo.countByIsDeletedFalse();
    }

    @Override
    public Page<Category> findActiveCategoriesWithPagination(int page, int size) {
        int safePage = page < 1 ? 0 : page - 1;
        return repo.findByIsDeletedFalse(PageRequest.of(safePage, size));
    }

    @Override
    public Page<Category> searchCategoriesWithPagination(String searchTerm, int page, int size) {
        int safePage = page < 1 ? 0 : page - 1;
        return repo.findByNameContainingIgnoreCaseAndIsDeletedFalse(searchTerm, PageRequest.of(safePage, size));
    }
}