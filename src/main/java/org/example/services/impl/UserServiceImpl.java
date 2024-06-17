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
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }


    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

}