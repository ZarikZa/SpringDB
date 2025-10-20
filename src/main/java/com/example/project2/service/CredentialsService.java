package com.example.project2.service;

import com.example.project2.model.Credentials;
import com.example.project2.repository.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CredentialsService {
    List<Credentials> findAllCredentials();
    Credentials findCredentialsById(Long id);
    Credentials addCredentials(Credentials credentials);
    Credentials updateCredentials(Credentials credentials);
    void deleteCredentials(Long id);
    List<Credentials> searchCredentials(String searchTerm);
    List<Credentials> getCredentialsWithPagination(int page, int size);
    long getTotalCredentialsCount();
}