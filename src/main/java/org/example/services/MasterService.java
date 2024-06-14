package org.example.services;


import org.example.entities.Document;
import org.example.entities.MasterAccessRequest;
import org.example.pojo.MasterDTO;
import org.example.pojo.MasterInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MasterService {

    MasterDTO getMaster(Long id);

    void createMaster(MasterAccessRequest request, Long userId, List<Document> documents);

    MasterInfoDTO getMasterInfo(Long id);

    Page<MasterDTO> getAllMasters(Pageable pageable);
}
