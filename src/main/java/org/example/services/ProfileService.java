package org.example.services;

import org.example.pojo.ChangeProfileDTO;
import org.example.pojo.ProfileDTO;
import org.springframework.stereotype.Service;

@Service
public interface ProfileService {

    ProfileDTO getProfileInfo(Long id);
    Long changeProfile(Long id, ChangeProfileDTO profileDTO);

}
