package com.example.project2.controller;

import com.example.project2.model.Toilet;
import com.example.project2.model.Category;
import com.example.project2.service.ToiletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/toilets")
public class ToiletController {
    @Autowired
    private ToiletService toiletService;

    @GetMapping
    public String listToilets(
            @RequestParam(value = "search", required = false) String searchTerm,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        List<Toilet> filtered = toiletService.findActiveToilets();

        if (searchTerm != null && !searchTerm.isEmpty()) {
            filtered = toiletService.searchToilets(searchTerm);
        } else if (brand != null || maxPrice != null || categoryId != null) {
            filtered = toiletService.filterToilets(brand, maxPrice, categoryId);
        }

        long totalCount = filtered.size();
        int totalPages = (int) Math.ceil((double) totalCount / size);
        if (page > totalPages) page = totalPages;

        List<Toilet> pagedToilets = toiletService.getToiletsWithPagination(page, size, filtered);

        model.addAttribute("toilets", pagedToilets);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("brands", toiletService.getAllBrands());
        model.addAttribute("materials", toiletService.getAllMaterials());
        model.addAttribute("categories", toiletService.getAllCategories());
        model.addAttribute("selectedBrand", brand);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("selectedMaxPrice", maxPrice);

        return "toilet";
    }

    @GetMapping("/edit/{id}")
    public String editToiletForm(@PathVariable Long id, Model model) {
        Toilet toilet = toiletService.findToiletById(id);
        if (toilet == null) {
            return "redirect:/toilets";
        }
        model.addAttribute("toilet", toilet);
        model.addAttribute("categories", toiletService.getAllCategories());
        return "edit-toilet";
    }

    @PostMapping("/add")
    public String addToilet(@ModelAttribute Toilet toilet) {
        toiletService.addToilet(toilet);
        return "redirect:/toilets";
    }

    @PostMapping("/update")
    public String updateToilet(@ModelAttribute Toilet toilet) {
        toiletService.updateToilet(toilet);
        return "redirect:/toilets";
    }

    @PostMapping("/delete")
    public String deleteToilet(@RequestParam Long id) {
        toiletService.deleteToilet(id);
        return "redirect:/toilets";
    }

    @PostMapping("/soft-delete")
    public String softDeleteToilet(@RequestParam Long id) {
        toiletService.softDeleteToilet(id);
        return "redirect:/toilets";
    }

    @PostMapping("/delete-multiple")
    public String deleteMultipleToilets(@RequestParam List<Long> ids) {
        toiletService.deleteMultipleToilets(ids);
        return "redirect:/toilets";
    }

    @PostMapping("/soft-delete-multiple")
    public String softDeleteMultipleToilets(@RequestParam List<Long> ids) {
        toiletService.softDeleteMultipleToilets(ids);
        return "redirect:/toilets";
    }
}