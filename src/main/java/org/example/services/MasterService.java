package org.example.services;


import org.example.pojo.MasterDTO;
import org.example.pojo.MasterInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface MasterService {

    MasterDTO getMaster(Long id);

    MasterInfoDTO getMasterInfo(Long id);

    Page<MasterDTO> getAllMasters(Pageable pageable);
}
