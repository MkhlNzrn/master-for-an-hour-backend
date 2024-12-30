package org.example.services;

import org.example.entities.Bid;
import org.example.entities.Client;
import org.example.entities.User;
import org.example.pojo.ClientDTO;
import org.example.pojo.SignUpRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClientService {
     Long createClient(SignUpRequest signUpRequest, User userId);

     Long getClientByUserUsername(String username);

     Long chooseBid(Long id);

     List<Bid> getAllBids(Long id);

     Page<ClientDTO> getAllClients(Pageable pageable);
}
