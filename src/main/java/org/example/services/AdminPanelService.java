package org.example.services;

import org.example.pojo.SignUpRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface AdminPanelService {
    void createMasterAccountRequest(SignUpRequest request) throws IOException;

    Long acceptMasterAccessRequest(Long id);

    Long discardMasterAccessRequest(Long id);
}
