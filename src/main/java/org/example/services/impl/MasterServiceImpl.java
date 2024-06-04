package org.example.services.impl;


import org.example.dtos.MasterDTO;
import org.example.dtos.MasterInfoDTO;
import org.example.entites.Master;
import org.example.exceptions.MasterNotFoundException;
import org.example.exceptions.NoMastersFoundException;
import org.example.repositories.MasterRepository;
import org.example.services.MasterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MasterServiceImpl implements MasterService {

    private final MasterRepository masterRepository;

    public MasterServiceImpl(MasterRepository masterRepository) {
        this.masterRepository = masterRepository;
    }

    @Override
    public MasterDTO getMaster(Long id) {
        return convertToDTO(masterRepository.findById(id).orElseThrow(() -> new MasterNotFoundException(id)));
    }

    @Override
    public MasterInfoDTO getMasterInfo(Long id) {
        Master master = masterRepository.findById(id).orElseThrow(() -> new MasterNotFoundException(id));
        return MasterInfoDTO.builder()
                .fullName(master.getFullName())
                .description(master.getDescription())
                .age(master.getAge())
                .rate(master.getRate())
                .photoLink(master.getPhotoLink())
                .documents(master.getDocuments())
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
                .fullName(master.getFullName())
                .email(master.getEmail())
                .phoneNumber(master.getPhoneNumber())
                .telegramTag(master.getTelegramTag())
                .description(master.getDescription())
                .age(master.getAge())
                .rate(master.getRate())
                .photoLink(master.getPhotoLink())
                .documents(master.getDocuments())
                .build();
    }

}
