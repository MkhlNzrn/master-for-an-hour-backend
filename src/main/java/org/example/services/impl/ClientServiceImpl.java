package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.Client;
import org.example.pojo.SignUpRequest;
import org.example.repositories.ClientRepository;
import org.example.services.ClientService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    @Override
    public void createClient(SignUpRequest signUpRequest, Long userId) {
        Client client = new Client(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getPhoneNumber(),
                signUpRequest.getTelegramTag(),
                userId
        );
        clientRepository.save(client);
    }
}
