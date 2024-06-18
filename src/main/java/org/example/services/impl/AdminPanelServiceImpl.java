package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.exceptions.UserEmailAlreadyExistsException;
import org.example.entities.Document;
import org.example.entities.MasterAccessRequest;
import org.example.entities.User;
import org.example.enums.ERole;
import org.example.exceptions.MasterAccessRequestNotFoundException;
import org.example.pojo.SignUpRequest;
import org.example.repositories.DocumentRepository;
import org.example.repositories.MasterAccessRequestRepository;
import org.example.services.AdminPanelService;
import org.example.services.MasterService;
import org.example.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPanelServiceImpl implements AdminPanelService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final MasterService masterService;
    private final MasterAccessRequestRepository masterAccessRequestRepository;
    private final DocumentRepository documentRepository;

    @Override
    public void createMasterAccountRequest(SignUpRequest request) {
        if (userService.userExists(request.getUsername()) || masterAccessRequestRepository.existsByEmail(request.getUsername()))
            throw new UserEmailAlreadyExistsException(request.getUsername());
        MasterAccessRequest masterRequest = masterAccessRequestRepository.save(
                new MasterAccessRequest(
                        request.getFirstName(),
                        request.getMiddleName(),
                        request.getLastName(),
                        request.getUsername(),
                        request.getPassword(),
                        request.getRole(),
                        request.getPhoneNumber(),
                        request.getTelegramTag(),
                        request.getDescription(),
                        request.getAge(),
                        request.getRate(),
                        request.getPhotoLink()
                )
        );
        request.getDocuments().forEach(doc -> {
            doc.setMasterAccessRequest(masterRequest);
            doc.setName(Paths.get(doc.getUrl()).getFileName().toString());
        });
        documentRepository.saveAll(request.getDocuments());
    }

    @Override
    public Long acceptMasterAccessRequest(Long id) {
        MasterAccessRequest request = masterAccessRequestRepository.findById(id).orElseThrow(() -> new MasterAccessRequestNotFoundException(id));

        User user = User.builder()
                .username(request.getEmail())
                .firstName(request.getFirstName())
                .password(passwordEncoder.encode(request
                        .getPassword())).role(ERole.valueOf(request.getRole()))
                .build();

        userService.create(user);
        List<Document> documents = documentRepository.findByMasterAccessRequest(request);
        masterService.createMaster(request, user.getId(), documents);
        documents.forEach(doc -> {
            doc.setMasterAccessRequest(null);
        });
        documentRepository.saveAll(documents);
        masterAccessRequestRepository.delete(request);
        return id;
    }

    @Override
    public Long discardMasterAccessRequest(Long id) {
        MasterAccessRequest masterAccessRequest = masterAccessRequestRepository.findById(id).orElseThrow(() -> new MasterAccessRequestNotFoundException(id));
        List<Document> documents = documentRepository.findByMasterAccessRequest(masterAccessRequest);
        documentRepository.deleteAll(documents);
        masterAccessRequestRepository.delete(masterAccessRequest);
        return id;
    }
}
