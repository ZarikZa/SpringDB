package com.example.project2.service;

import com.example.project2.model.Order;
import com.example.project2.model.OrderItem;
import com.example.project2.model.Toilet;
import com.example.project2.model.Category;
import com.example.project2.repository.ToiletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ToiletServiceImpl implements ToiletService {
    @Autowired private ToiletRepository repo;
    @Autowired private CategoryService categoryService;

    @Override
    public List<Toilet> findAllToilets() {
        return repo.findAll();
    }

    @Override
    public List<Toilet> findActiveToilets() {
        return repo.findAllActiveForDropdown();
    }

    @Override
    public Toilet findToiletById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public Toilet addToilet(Toilet toilet) {
        return repo.save(toilet);
    }

    @Override
    public Toilet updateToilet(Toilet toilet) {
        return repo.save(toilet);
    }

    @Override
    public void deleteToilet(Long id) {
        repo.deleteById(id);
    }

    @Override
    public boolean softDeleteToilet(Long id) {
        Toilet toilet = findToiletById(id);
        if (toilet != null && toilet.isActive()) {
            toilet.setActive(false);
            repo.save(toilet);
            return true;
        }
        return false;
    }

    @Override
    public void deleteMultipleToilets(List<Long> ids) {
        repo.deleteAllById(ids);
    }

    @Override
    public void softDeleteMultipleToilets(List<Long> ids) {
        repo.findAllById(ids).forEach(toilet -> {
            if (toilet.isActive()) {
                toilet.setActive(false);
                repo.save(toilet);
            }
        });
    }

    @Override
    public List<Toilet> searchToilets(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) return findActiveToilets();
        String term = searchTerm.toLowerCase();
        Pageable pageable = Pageable.unpaged();
        return repo.findByBrandContainingIgnoreCaseAndIsActiveTrue(searchTerm, pageable).getContent()
                .stream()
                .filter(toilet ->
                        toilet.getName().toLowerCase().contains(term) ||
                                toilet.getModel().toLowerCase().contains(term) ||
                                toilet.getColor().toLowerCase().contains(term) ||
                                toilet.getMaterial().toLowerCase().contains(term))
                .collect(Collectors.toList());
    }

    @Override
    public List<Toilet> filterToilets(String brand, Double maxPrice, Long categoryId) {
        Pageable pageable = Pageable.unpaged();
        if (brand != null && !brand.isEmpty()) {
            return repo.findByBrandContainingIgnoreCaseAndIsActiveTrue(brand, pageable).getContent();
        }
        if (maxPrice != null) {
            return repo.findByIsActiveTrue(pageable).getContent()
                    .stream()
                    .filter(toilet -> toilet.getPrice().doubleValue() <= maxPrice)
                    .collect(Collectors.toList());
        }
        if (categoryId != null) {
            return repo.findByCategoryIdAndIsActiveTrue(categoryId, pageable).getContent();
        }
        return findActiveToilets();
    }

    @Override
    public List<Toilet> getToiletsWithPagination(int page, int size, List<Toilet> sourceList) {
        int safePage = page < 1 ? 0 : page - 1;
        return repo.findByIsActiveTrue(PageRequest.of(safePage, size)).getContent();
    }

    @Override
    public long getTotalToiletsCount(List<Toilet> sourceList) {
        return repo.count();
    }

    @Override
    public List<String> getAllBrands() {
        return repo.findByIsActiveTrue(Pageable.unpaged()).getContent()
                .stream()
                .map(Toilet::getBrand)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllMaterials() {
        return repo.findByIsActiveTrue(Pageable.unpaged()).getContent()
                .stream()
                .map(Toilet::getMaterial)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryService.findActiveCategories();
    }

    @Override
    public Category findCategoryById(Long id) {
        return categoryService.findCategoryById(id);
    }

    @Override
    public String getCategoryNameById(Long categoryId) {
        Category category = categoryService.findCategoryById(categoryId);
        return category != null ? category.getName() : "Неизвестно";
    }

    @Override
    public List<Toilet> findByIds(List<Long> ids) {
        return repo.findAllById(ids);
    }


    @Override
    public List<OrderItem> createOrderItemsForToilets(List<Toilet> toilets, Order order) {
        return toilets.stream().map(toilet -> {
            OrderItem item = new OrderItem();
            item.setToilet(toilet);
            item.setOrder(order);
            item.setQuantity(1);
            item.setPrice(toilet.getPrice());
            return item;
        }).collect(Collectors.toList());
    }

}
