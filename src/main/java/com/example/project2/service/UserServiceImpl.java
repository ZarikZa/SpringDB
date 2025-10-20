package com.example.project2.service;

import com.example.project2.model.User;
import com.example.project2.model.UserRole;
import com.example.project2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired private UserRepository repo;

    @Override
    public List<User> findAllUsers() {
        return repo.findAll();
    }

    @Override
    public List<User> findActiveUsers() {
        return repo.findByIsDeletedFalseAndIsActiveTrue(Pageable.unpaged()).getContent();
    }

    @Override
    public User findUserById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public User addUser(User user) {
        return repo.save(user);
    }

    @Override
    public User updateUser(User user) {
        return repo.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        repo.deleteById(id);
    }

    @Override
    public boolean softDeleteUser(Long id) {
        User user = findUserById(id);
        if (user != null && !user.isDeleted()) {
            user.setDeleted(true);
            repo.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void deleteMultipleUsers(List<Long> ids) {
        repo.deleteAllById(ids);
    }

    @Override
    public void softDeleteMultipleUsers(List<Long> ids) {
        repo.findAllById(ids).forEach(user -> {
            if (!user.isDeleted()) {
                user.setDeleted(true);
                repo.save(user);
            }
        });
    }

    @Override
    public List<User> searchUsers(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findActiveUsers();
        }

        String trimmedTerm = searchTerm.trim();

        List<User> byEmail = repo.findByEmailContainingIgnoreCaseAndIsDeletedFalseAndIsActiveTrue(
                trimmedTerm, Pageable.unpaged()).getContent();

        List<User> byFirstName = repo.findByFirstNameContainingIgnoreCaseAndIsDeletedFalseAndIsActiveTrue(
                trimmedTerm, Pageable.unpaged()).getContent();

        Set<User> resultSet = new LinkedHashSet<>(byEmail);
        resultSet.addAll(byFirstName);

        return new ArrayList<>(resultSet);
    }

    @Override
    public List<User> filterUsers(UserRole role) {
        return repo.findByRoleAndIsDeletedFalseAndIsActiveTrue(role, Pageable.unpaged()).getContent();
    }

    @Override
    public List<User> getUsersWithPagination(int page, int size, List<User> sourceList) {
        int safePage = (page < 1) ? 0 : page - 1;
        int start = safePage * size;
        int end = Math.min(start + size, sourceList.size());

        if (start > sourceList.size()) {
            return List.of();
        }

        return sourceList.subList(start, end);
    }

    @Override
    public long getTotalUsersCount(List<User> sourceList) {
        return repo.count();
    }
}