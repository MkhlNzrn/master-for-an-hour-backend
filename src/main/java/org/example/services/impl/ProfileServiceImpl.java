package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.Client;
import org.example.entities.Master;
import org.example.entities.User;
import org.example.entities.*;

import java.util.ArrayList;
import java.util.List;

import org.example.exceptions.*;
import org.example.pojo.ChangeProfileDTO;
import org.example.pojo.ProfileDTO;
import org.example.repositories.*;
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
    private final CategoryRepository categoryRepository;
    private final MetroStationRepository metroStationRepository;


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
                    .categories(convertCategoriesToString(master.getCategories()))
                    .lastName(master.getLastName())
                    .metroStation(convertMetroStationsToString(master.getMetroStation()))
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

    @Override
    public Long changeProfile(Long id, ChangeProfileDTO profileDTO) {  //TODO: refactor
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (user.getRole().name().equals("ROLE_MASTER")) {
            Master profile = masterRepository.findByUserId(id)
                    .orElseThrow(() -> new MasterNotFoundException(id));
            if (!profileDTO.getEmail().isEmpty() && !profileDTO.getEmail().equals(profile.getEmail())) {
                profile.setEmail(profileDTO.getEmail());
                user.setUsername(profileDTO.getEmail());
                userRepository.save(user);
            }
            if (!profileDTO.getTelegramTag().isEmpty() && !profileDTO.getTelegramTag().equals(profile.getTelegramTag()))
                profile.setTelegramTag(profileDTO.getTelegramTag());
            if (!profileDTO.getPhoneNumber().isEmpty() && !profileDTO.getPhoneNumber().equals(profile.getPhoneNumber()))
                profile.setPhoneNumber(profileDTO.getPhoneNumber());
            if (profile.getUser().getRole().name().equals("ROLE_MASTER")) {
                if (!profileDTO.getDescription().isEmpty() && !profileDTO.getDescription().equals(profile.getDescription()))
                    profile.setDescription(profileDTO.getDescription());
                if (!profileDTO.getCategories().isEmpty() && !profileDTO.getCategories().equals(convertCategoriesToString(profile.getCategories())))
                    profile.setCategories(convertStringsToCategories(profileDTO.getCategories()));
                if (!profileDTO.getMetroStation().isEmpty() && !profileDTO.getMetroStation().equals(profile.getMetroStation()))
                    profile.setMetroStation(convertStringsToMetroStations(profileDTO.getMetroStation()));
            }
            return masterRepository.save(profile).getUser().getId();
        } else if (user.getRole().name().equals("ROLE_CLIENT")) {
            Client profile = clientRepository.findByUserId(id)
                    .orElseThrow(() -> new ClientNotFoundException(id));
            if (!profileDTO.getEmail().isEmpty() && !profileDTO.getEmail().equals(profile.getEmail())) {
                profile.setEmail(profileDTO.getEmail());
                user.setUsername(profileDTO.getEmail());
                userRepository.save(user);
            }
            if (!profileDTO.getTelegramTag().isEmpty() && !profileDTO.getTelegramTag().equals(profile.getTelegramTag()))
                profile.setTelegramTag(profileDTO.getTelegramTag());
            if (!profileDTO.getPhoneNumber().isEmpty() && !profileDTO.getPhoneNumber().equals(profile.getPhoneNumber()))
                profile.setPhoneNumber(profileDTO.getPhoneNumber());
            return clientRepository.save(profile).getUser().getId();
        } else {
            throw new InvalidRoleException(user.getRole().name());
        }
    }

    private List<String> convertCategoriesToString(List<Category> categories) {
        List<String> stringCategories = new ArrayList<>();
        for (Category category : categories) {
            stringCategories.add(category.getName());
        }
        return stringCategories;
    }

    private List<Category> convertStringsToCategories(List<String> stringCategories) {
        List<Category> categories = new ArrayList<>();
        for (String categoryName : stringCategories) {
            categories.add(categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new CategoryNotFoundException(categoryName)));
        }
        return categories;
    }

    private List<MetroStation> convertStringsToMetroStations(List<String> stringMetroStations) {
        List<MetroStation> metroStations = new ArrayList<>();
        for (String metroStation : stringMetroStations) {
            metroStations.add(metroStationRepository.findByName(metroStation).orElseThrow(() -> new MetroStationNotFoundException(metroStation)));
        }
        return metroStations;
    }

    private List<String> convertMetroStationsToString(List<MetroStation> metroStations) {
        List<String> stringMetroStations = new ArrayList<>();
        for (MetroStation metroStation : metroStations) {
            stringMetroStations.add(metroStation.getName());
        }
        return stringMetroStations;
    }
}
