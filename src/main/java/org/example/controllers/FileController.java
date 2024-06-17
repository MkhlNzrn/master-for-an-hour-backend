package org.example.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Tag(name = "Operations with Masters")
public class FileController {

    @PostMapping("/photo")
    public String uploadPhoto(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String destination = "/root/media/photos/" + multipartFile.getOriginalFilename();
        File file = new File(destination);
        multipartFile.transferTo(file);
        return multipartFile.getOriginalFilename();
    }

    @PostMapping("/document")
    public String uploadDocument(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        String destination = "/root/media/documents/" + multipartFile.getOriginalFilename();
        File file = new File(destination);
        multipartFile.transferTo(file);
        return multipartFile.getOriginalFilename();
    }
}
