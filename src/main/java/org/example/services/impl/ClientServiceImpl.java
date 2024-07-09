package org.example.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.entities.*;
import org.example.exceptions.BidNotFoundException;
import org.example.exceptions.ClientNotFoundException;
import org.example.exceptions.TaskNotFoundException;
import org.example.pojo.SignUpRequest;
import org.example.repositories.BidRepository;
import org.example.repositories.ClientRepository;
import org.example.repositories.TaskRepository;
import org.example.repositories.UserRepository;
import org.example.services.ClientService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final BidRepository bidRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

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

    @Transactional
    @Override
    public Long chooseBid(Long id) {
        Bid bid = bidRepository.findById(id).orElseThrow(() -> new BidNotFoundException(id));
        Master master = bid.getMaster();
        Task task = bid.getTask();
        task.setMaster(master);
        taskRepository.save(task);
        bidRepository.deleteAllByTask(task);
        return master.getId();
    }

    @Override
    public List<Bid> getAllBids(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        return bidRepository.findAllByTask(task);
    }
}
