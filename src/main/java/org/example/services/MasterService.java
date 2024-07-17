package org.example.services;


import org.example.entities.User;
import org.example.pojo.*;
import org.example.wrappers.PathSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;

@Service
public interface MasterService {

    MasterDTO getMaster(Long id);

    MasterInfoDTO getMasterInfo(Long id);

    Page<MasterDTO> getAllMasters(Pageable pageable);

    List<String> getMetroStations();

    String uploadPhotoReg(MultipartFile multipartFile, String username) throws IOException;

    PathSet<String> uploadDocument(List<MultipartFile> files, String username) throws IOException;

    String uploadPhoto(MultipartFile multipartFile, String username) throws IOException;

    Long createMasterAccountRequest(SignUpRequest request, User user) throws IOException;

    Long acceptMasterAccessRequest(Long id);

    Long discardMasterAccessRequest(Long id) throws IOException;

    Long deleteMaster(String username) throws IOException;

    Long getMasterByUserUsername(String username);

    void sendValidationMsgToEmail(String email);

    InputStream getPhoto(Long id) throws MalformedURLException, FileNotFoundException;

    InputStream getPhoto(String key) throws MalformedURLException, FileNotFoundException;

    List<MasterDTO> getTop10MastersByRate();

    void validateEmail(String email, Long pin);

    Long toBid(BidDTO bidDTO);

    Long verifyDocks(Long id);

    List<GetFeedbackResponse> getFeedbacks(Long id);
}
