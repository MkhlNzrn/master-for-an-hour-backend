package org.example.services;

import org.example.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User save(User user);
    User create(User user);
    User getByUsername(String username);
    UserDetailsService userDetailsService();
    User getCurrentUser();
    boolean userExists(String username);
    void deleteByEmail(String username);
    Long setUserIsBanned(String username, boolean isBanned);
}
