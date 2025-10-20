package com.example.project2.controller;

import com.example.project2.model.User;
import com.example.project2.model.UserRole;
import com.example.project2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String listUsers(
            @RequestParam(value = "search", required = false) String searchTerm,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        List<User> users;

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            users = userService.searchUsers(searchTerm);
        } else {
            users = userService.findActiveUsers();
        }

        long totalCount = users.size();
        int totalPages = (int) Math.ceil((double) totalCount / size);

        if (page > totalPages && totalPages > 0) {
            page = totalPages;
        } else if (page < 1) {
            page = 1;
        }

        List<User> pagedUsers = userService.getUsersWithPagination(page, size, users);

        model.addAttribute("roles", Arrays.asList(UserRole.values()));
        model.addAttribute("users", pagedUsers);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("searchTerm", searchTerm);

        return "user";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user) {
        userService.addUser(user);
        return "redirect:/users";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute User user) {
        User existingUser = userService.findUserById(user.getId());
        if (existingUser != null) {
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPhone(user.getPhone());
            existingUser.setRole(user.getRole());

            userService.updateUser(existingUser);
        }
        return "redirect:/users";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    @PostMapping("/soft-delete")
    public String softDeleteUser(@RequestParam Long id) {
        userService.softDeleteUser(id);
        return "redirect:/users";
    }

    @PostMapping("/delete-multiple")
    public String deleteMultipleUsers(@RequestParam List<Long> ids) {
        userService.deleteMultipleUsers(ids);
        return "redirect:/users";
    }

    @PostMapping("/soft-delete-multiple")
    public String softDeleteMultipleUsers(@RequestParam List<Long> ids) {
        userService.softDeleteMultipleUsers(ids);
        return "redirect:/users";
    }
}