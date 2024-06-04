package org.example.services;


import org.example.dtos.MasterDTO;
import org.example.dtos.MasterInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface MasterService {

    MasterDTO getMaster(Long id);

    MasterInfoDTO getMasterInfo(Long id);

    Page<MasterDTO> getAllMasters(Pageable pageable);
}
