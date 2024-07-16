package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.Client;
import org.example.entities.Master;
import org.example.entities.User;
import org.example.exceptions.ClientNotFoundException;
import org.example.exceptions.InvalidRoleException;
import org.example.exceptions.MasterNotFoundException;
import org.example.pojo.ProfileDTO;
import org.example.repositories.ClientRepository;
import org.example.repositories.DocumentRepository;
import org.example.repositories.MasterRepository;
import org.example.repositories.UserRepository;
import org.example.services.ProfileService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final MasterRepository masterRepository;
    private final DocumentRepository documentRepository;
    private final ClientRepository clientRepository;

    @Override
    public ProfileDTO getProfileInfo(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (user.getRole().name().equals("ROLE_MASTER")) {
            Master master = masterRepository.findByEmail(user.getUsername())
                    .orElseThrow(() -> new MasterNotFoundException(user.getUsername()));
            return ProfileDTO.builder()
                    .role("ROLE_MASTER")
                    .id(master.getId())
                    .firstName(master.getFirstName())
                    .middleName(master.getMiddleName())
.categories(master.getCategories())
                    .lastName(master.getLastName())
                    .metroStation(master.getMetroStation())
                    .description(master.getDescription())
                    .email(master.getEmail())
                    .age(master.getAge())
                    .phoneNumber(master.getPhoneNumber())
                    .telegramTag(master.getTelegramTag())
                    .rate(master.getRate())
                    .isAccepted(master.getIsAccepted())
                    .photoLink(master.getPhotoLink())
                    .documents(documentRepository.findByMaster(master))
                    .userId(master.getUser().getId())
                    .build();
        } else if (user.getRole().name().equals("ROLE_CLIENT")) {
            Client client = clientRepository.findByEmail(user.getUsername())
                    .orElseThrow(() -> new ClientNotFoundException(user.getUsername()));
            return ProfileDTO.builder()
                    .role("ROLE_CLIENT")
                    .id(client.getId())
                    .firstName(client.getName())
                    .email(client.getEmail())
                    .phoneNumber(client.getPhoneNumber())
                    .telegramTag(client.getTelegramTag())
                    .userId(client.getUser().getId())
                    .build();
        } else {
            throw new InvalidRoleException(user.getRole().name());
        }
    }
}
