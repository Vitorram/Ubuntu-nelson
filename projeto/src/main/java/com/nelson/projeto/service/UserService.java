package com.nelson.projeto.service;

import com.nelson.projeto.model.User;
import com.nelson.projeto.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User create(User user) {

        if (user.getEnderecos() != null) {
            user.getEnderecos().forEach(e -> e.setUser(user));
        }

        if (user.getDocumentos() != null) {
            user.getDocumentos().forEach(d -> d.setUser(user));
        }

        return repo.save(user);
    }

    public List<User> list() {
        return (List<User>) repo.findAll();
    }

    public User get(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID deve ser um número válido");
        }
        return repo.findById(id).orElse(null);
    }

    public User update(Long id, User user) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID deve ser um número válido");
        }
        if (user == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }
        user.setId(id);

        if (user.getEnderecos() != null) {
            user.getEnderecos().forEach(e -> e.setUser(user));
        }

        if (user.getDocumentos() != null) {
            user.getDocumentos().forEach(d -> d.setUser(user));
        }

        return repo.save(user);
    }

    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID deve ser um número válido");
        }
        repo.deleteById(id);
    }
}
