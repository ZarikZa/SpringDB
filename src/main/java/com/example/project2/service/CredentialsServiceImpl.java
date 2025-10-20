package com.example.project2.service;

import com.example.project2.model.Credentials;
import com.example.project2.repository.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CredentialsServiceImpl implements CredentialsService {
    @Autowired private CredentialsRepository repo;

    @Override
    public List<Credentials> findAllCredentials() { return repo.findAll(); }
    @Override
    public Credentials findCredentialsById(Long id) { return repo.findById(id).orElse(null); }
    @Override
    public Credentials addCredentials(Credentials credentials) { return repo.save(credentials); }
    @Override
    public Credentials updateCredentials(Credentials credentials) { return repo.save(credentials); }
    @Override
    public void deleteCredentials(Long id) { repo.deleteById(id); }
    @Override
    public List<Credentials> searchCredentials(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) return findAllCredentials();
        return repo.findByUsernameContainingIgnoreCaseAndIsLockedFalse(searchTerm, Pageable.unpaged()).getContent();
    }
    @Override
    public List<Credentials> getCredentialsWithPagination(int page, int size) {
        return repo.findAll(PageRequest.of(page - 1, size)).getContent();
    }
    @Override
    public long getTotalCredentialsCount() { return repo.count(); }
}