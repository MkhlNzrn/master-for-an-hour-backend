package org.example.services;

import org.example.pojo.SignUpRequest;
import org.springframework.stereotype.Service;

@Service
public interface AdminPanelService {
    void createMasterAccountRequest(SignUpRequest request);

    Long acceptMasterAccessRequest(Long id);

    Long discardMasterAccessRequest(Long id);
}
