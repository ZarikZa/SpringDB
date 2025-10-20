package com.example.project2.controller;

import com.example.project2.model.Category;
import com.example.project2.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listCategories(
            @RequestParam(value = "search", required = false) String searchTerm,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        List<Category> categories;
        long totalCount;

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            categories = categoryService.searchCategories(searchTerm);
            totalCount = categories.size();
        } else {
            categories = categoryService.findActiveCategories();
            totalCount = categories.size();
        }

        int totalPages = (int) Math.ceil((double) totalCount / size);
        if (page > totalPages && totalPages > 0) page = totalPages;
        if (page < 1) page = 1;

        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, categories.size());
        List<Category> pagedCategories = categories.subList(startIndex, endIndex);

        model.addAttribute("categories", pagedCategories);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("searchTerm", searchTerm);

        return "categories";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute Category category) {
        categoryService.addCategory(category);
        return "redirect:/categories";
    }

    @PostMapping("/update")
    public String updateCategory(@ModelAttribute Category category) {
        categoryService.updateCategory(category);
        return "redirect:/categories";
    }

    @PostMapping("/delete")
    public String deleteCategory(@RequestParam Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }

    @PostMapping("/soft-delete")
    public String softDeleteCategory(@RequestParam Long id) {
        categoryService.softDeleteCategory(id);
        return "redirect:/categories";
    }

    @PostMapping("/multiple-action")
    public String multipleAction(@RequestParam(required = false) List<Long> ids,
                                 @RequestParam(required = false) String action) {
        if (ids == null || ids.isEmpty() || action == null || action.isEmpty()) {
            return "redirect:/categories";
        }
        if ("delete".equals(action)) {
            categoryService.deleteMultipleCategories(ids);
        } else if ("soft-delete".equals(action)) {
            categoryService.softDeleteMultipleCategories(ids);
        }
        return "redirect:/categories";
    }
}