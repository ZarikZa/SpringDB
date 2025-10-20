package com.example.project2.controller;

import com.example.project2.model.Order;
import com.example.project2.model.OrderStatus;
import com.example.project2.model.Toilet;
import com.example.project2.model.User;
import com.example.project2.service.OrderService;
import com.example.project2.service.ToiletService;
import com.example.project2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ToiletService toiletService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listOrders(
            @RequestParam(value = "search", required = false) String searchTerm,
            @RequestParam(value = "status", required = false) OrderStatus status,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        List<Order> orders;

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            orders = orderService.searchOrders(searchTerm.trim());
            model.addAttribute("searchTerm", searchTerm.trim());
        } else if (status != null || userId != null) {
            orders = orderService.filterOrders(status, userId);
            model.addAttribute("selectedStatus", status);
            model.addAttribute("selectedUserId", userId);
        } else {
            orders = orderService.findActiveOrders();
        }

        model.addAttribute("statuses", OrderStatus.values());
        model.addAttribute("users", userService.findActiveUsers());
        model.addAttribute("toilets", toiletService.findActiveToilets());

        long totalCount = orders.size();
        int totalPages = (int) Math.ceil((double) totalCount / size);

        if (page > totalPages && totalPages > 0) {
            page = totalPages;
        } else if (page < 1) {
            page = 1;
        }

        List<Order> pagedOrders = orderService.getOrdersWithPagination(page, size, orders);

        model.addAttribute("orders", pagedOrders);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalCount", totalCount);

        return "orders";
    }

    @GetMapping("/add")
    public String showAddOrderForm(Model model) {
        model.addAttribute("order", new Order());
        model.addAttribute("users", userService.findActiveUsers());
        model.addAttribute("toilets", toiletService.findActiveToilets());
        model.addAttribute("statuses", OrderStatus.values());
        return "order-form"; // или вернуть ту же страницу, если форма на той же странице
    }

    @PostMapping("/add")
    public String addOrder(@ModelAttribute Order order,
                           @RequestParam(required = false) List<Long> toiletIds) {
        if (toiletIds != null && !toiletIds.isEmpty()) {
            List<Toilet> toilets = toiletService.findByIds(toiletIds);
            order.setOrderItems(toiletService.createOrderItemsForToilets(toilets, order));
        }
        order.calculateTotalAmount();
        orderService.addOrder(order);
        return "redirect:/orders";
    }

    @GetMapping("/edit/{id}")
    public String showEditOrderForm(@PathVariable Long id, Model model) {
        Order order = orderService.findOrderById(id);
        if (order != null) {
            model.addAttribute("order", order);
            model.addAttribute("users", userService.findActiveUsers());
            model.addAttribute("toilets", toiletService.findActiveToilets());
            model.addAttribute("statuses", OrderStatus.values());
            return "order-form";
        }
        return "redirect:/orders";
    }

    @PostMapping("/update")
    public String updateOrder(@RequestParam Long id, @RequestParam OrderStatus status) {
        Order existingOrder = orderService.findOrderById(id);
        if (existingOrder != null) {
            existingOrder.setStatus(status);
            orderService.updateOrder(existingOrder);
        }
        return "redirect:/orders";
    }

    @PostMapping("/delete")
    public String deleteOrder(@RequestParam Long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }

    @PostMapping("/soft-delete")
    public String softDeleteOrder(@RequestParam Long id) {
        orderService.softDeleteOrder(id);
        return "redirect:/orders";
    }

    @PostMapping("/delete-multiple")
    public String deleteMultipleOrders(@RequestParam List<Long> ids) {
        orderService.deleteMultipleOrders(ids);
        return "redirect:/orders";
    }

    @PostMapping("/soft-delete-multiple")
    public String softDeleteMultipleOrders(@RequestParam List<Long> ids) {
        orderService.softDeleteMultipleOrders(ids);
        return "redirect:/orders";
    }
}