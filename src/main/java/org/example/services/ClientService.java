package org.example.services;

import org.example.pojo.SignUpRequest;
import org.springframework.stereotype.Service;

@Service
public interface ClientService {
     void createClient(SignUpRequest signUpRequest, Long userId);
}
