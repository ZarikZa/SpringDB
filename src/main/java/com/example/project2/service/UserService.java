package com.example.project2.service;


import com.example.project2.model.User;
import com.example.project2.model.UserRole;
import org.hibernate.query.Page;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;


public interface UserService {
    List<User> findAllUsers();
    List<User> findActiveUsers();
    User findUserById(Long id);
    User addUser(User user);
    User updateUser(User user);
    void deleteUser(Long id);
    boolean softDeleteUser(Long id);
    void deleteMultipleUsers(List<Long> ids);
    void softDeleteMultipleUsers(List<Long> ids);
    List<User> searchUsers(String searchTerm);
    List<User> filterUsers(UserRole role);
    List<User> getUsersWithPagination(int page, int size, List<User> sourceList);
    long getTotalUsersCount(List<User> sourceList);
}
