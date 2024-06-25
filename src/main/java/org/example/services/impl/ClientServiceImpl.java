package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.Client;
import org.example.entities.User;
import org.example.exceptions.ClientNotFoundException;
import org.example.pojo.SignUpRequest;
import org.example.repositories.ClientRepository;
import org.example.services.ClientService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    @Override
    public Long createClient(SignUpRequest signUpRequest, User user) {
        Client client = new Client(
                signUpRequest.getFirstName(),
                signUpRequest.getEmail(),
                signUpRequest.getPhoneNumber(),
                signUpRequest.getTelegramTag(),
                user
        );
        return clientRepository.save(client).getId();
    }

    @Override
    public Long getClientByUserUsername(String username) {
        return clientRepository.findByEmail(username).orElseThrow(() -> new ClientNotFoundException(username)).getId();
    }
}
