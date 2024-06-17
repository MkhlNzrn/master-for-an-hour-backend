package org.example.services.impl;


import lombok.RequiredArgsConstructor;
import org.example.entities.Document;
import org.example.entities.MasterAccessRequest;
import org.example.pojo.MasterDTO;
import org.example.pojo.MasterInfoDTO;
import org.example.entities.Master;
import org.example.exceptions.MasterNotFoundException;
import org.example.exceptions.NoMastersFoundException;
import org.example.repositories.DocumentRepository;
import org.example.repositories.MasterRepository;
import org.example.services.MasterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MasterServiceImpl implements MasterService {

    private final MasterRepository masterRepository;
    private final DocumentRepository documentRepository;

    @Override
    public MasterDTO getMaster(Long id) {
        return convertToDTO(masterRepository.findById(id).orElseThrow(() -> new MasterNotFoundException(id)));
    }

    @Override
    public void createMaster(MasterAccessRequest request, Long userId, List<Document> documents) {
        Master master = new Master(
                request.getFirstName(),
                request.getMiddleName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getTelegramTag(),
                request.getDescription(),
                request.getAge(),
                request.getRate(),
                request.getPhotoLink(),
                userId
        );
        masterRepository.save(master);
        documents.forEach(doc -> {
            doc.setMaster(master);
        });
        documentRepository.saveAll(documents);
    }

    @Override
    public MasterInfoDTO getMasterInfo(Long id) {
        Master master = masterRepository.findById(id).orElseThrow(() -> new MasterNotFoundException(id));
        return MasterInfoDTO.builder()
                .firstName(master.getFirstName())
                .middleName(master.getMiddleName())
                .lastName(master.getLastName())
                .description(master.getDescription())
                .age(master.getAge())
                .rate(master.getRate())
                .photoLink(master.getPhotoLink())
                .documents(documentRepository.findByMaster(master))
                .build();
    }

    @Override
    public Page<MasterDTO> getAllMasters(Pageable pageable) {
        Page<Master> masters = masterRepository.findAllMastersPage(pageable);
        if (masters.isEmpty()) throw new NoMastersFoundException();
        return masters.map(this::convertToDTO);
    }

    private MasterDTO convertToDTO(Master master) {
        return MasterDTO.builder()
                .id(master.getId())
                .firstName(master.getFirstName())
                .middleName(master.getMiddleName())
                .lastName(master.getLastName())
                .email(master.getEmail())
                .phoneNumber(master.getPhoneNumber())
                .telegramTag(master.getTelegramTag())
                .description(master.getDescription())
                .age(master.getAge())
                .rate(master.getRate())
                .photoLink(master.getPhotoLink())
                .documents(documentRepository.findByMaster(master))
                .build();
    }

}
