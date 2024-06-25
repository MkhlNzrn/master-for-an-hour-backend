package org.example.services;

import org.example.entities.User;
import org.example.pojo.SignUpRequest;
import org.springframework.stereotype.Service;

@Service
public interface ClientService {
     Long createClient(SignUpRequest signUpRequest, User userId);

     Long getClientByUserUsername(String username);
}
