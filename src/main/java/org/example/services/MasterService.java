package org.example.services;


import org.example.entities.User;
import org.example.pojo.MasterDTO;
import org.example.pojo.MasterInfoDTO;
import org.example.pojo.SignUpRequest;
import org.example.wrappers.PathSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface MasterService {

    MasterDTO getMaster(Long id);

    MasterInfoDTO getMasterInfo(Long id);

    Page<MasterDTO> getAllMasters(Pageable pageable);

    List<String> getMetroStations();

    PathSet<String> uploadDocument(List<MultipartFile> files, String username) throws IOException;

    String uploadPhoto(MultipartFile multipartFile, String username) throws IOException;

    Long createMasterAccountRequest(SignUpRequest request, User user) throws IOException;

    Long acceptMasterAccessRequest(Long id);

    Long discardMasterAccessRequest(Long id) throws IOException;

    Long deleteMaster(String username) throws IOException;

    Long getMasterByUserUsername(String username);
}
