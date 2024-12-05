package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.User;
import org.example.exceptions.InvalidEmailException;
import org.example.exceptions.UserEmailAlreadyExistsException;
import org.example.exceptions.UserUsernameAlreadyExistsException;
import org.example.repositories.UserRepository;
import org.example.services.UserService;
import org.example.utils.EmailValidator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;


    public User save(User user) {
        return repository.save(user);
    }



    public User create(User user) {
        if (repository.existsByFirstName(user.getUsername())) {
            throw new UserUsernameAlreadyExistsException(user.getFirstName()
            );
        }

        if (repository.existsByUsername(user.getUsername())) {
            throw new UserEmailAlreadyExistsException(user.getUsername());
        }

        if (!EmailValidator.isValidEmail(user.getUsername())) {
            throw new InvalidEmailException(user.getUsername());
        }
        return save(user);
    }


    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + username));

    }


    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    private void setTimeOfBan(User user){
        LocalDateTime timeOfBan = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedDate = timeOfBan.format(formatter);

        user.setBanDate(formattedDate);
    }

    @Override
    @Transactional
    public Long setUserIsBanned(String username, boolean isBanned){
        var user = getByUsername(username);
        user.setIsBanned(isBanned);
        setTimeOfBan(user);

        repository.save(user);
        return user.getId();
    }

    @Override
    public boolean userExists(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public void deleteByEmail(String username) {
        User user = repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + username));
        repository.delete(user);
    }

}