package org.example.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface CategoryService {
    String uploadPhoto(MultipartFile multipartFile, Long id) throws IOException;
}
