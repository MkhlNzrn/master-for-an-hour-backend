package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.BidDTO;
import org.example.pojo.GetFeedbackResponse;
import org.example.pojo.MasterDTO;
import org.example.pojo.MasterInfoDTO;
import org.example.services.MasterService;
import org.example.wrappers.PathSet;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/masters")
@RequiredArgsConstructor
@Tag(name = "Operations with Masters")
public class MasterController {

    private final MasterService masterService;

    @Operation(description = "Get a full information about Master by ID")
    @GetMapping("/full/{id}")
    public ResponseEntity<MasterDTO> getMaster(@PathVariable Long id) {
        return ResponseEntity.ok(masterService.getMaster(id));
    }

    @Operation(description = "Get an information for client about Master by ID")
    @GetMapping("/info/{id}")
    public ResponseEntity<MasterInfoDTO> getMasterInfo(@PathVariable Long id) {
        return ResponseEntity.ok(masterService.getMasterInfo(id));
    }

    @Operation(description = "Get an information about all Masters")
    @GetMapping("/")
    public ResponseEntity<Page<MasterDTO>> getAllMasters(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(masterService.getAllMasters(pageRequest));
    }

    @Operation(description = "Get list of metro stations")
    @GetMapping("/metro-stations")
    public ResponseEntity<List<String>> getMetroStations() {
        return ResponseEntity.ok(masterService.getMetroStations());
    }

    @PostMapping("/documents")
    public ResponseEntity<PathSet<String>> uploadDocuments(@RequestParam("files") List<MultipartFile> multipartFiles, @RequestParam String username) throws IOException {
        return ResponseEntity.ok(masterService.uploadDocument(multipartFiles, username));
    }

    @PostMapping("/email")
    public ResponseEntity<String> sendValidationMsgToEmail(@RequestParam String email) {
        masterService.sendValidationMsgToEmail(email);
        return ResponseEntity.ok(email);
    }

    @DeleteMapping("/email")
    public ResponseEntity<String> validateEmail(@RequestParam String email, @RequestParam Long pin) {
        masterService.validateEmail(email, pin);
        return ResponseEntity.ok(email);
    }

    @PostMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile multipartFile, @RequestParam String username) throws IOException {
        return ResponseEntity.ok(masterService.uploadPhoto(multipartFile, username));
    }

    @PostMapping("/photo-reg")
    public ResponseEntity<String> uploadPhotoReg(@RequestParam("file") MultipartFile multipartFile, @RequestParam String username) throws IOException {
        return ResponseEntity.ok(masterService.uploadPhotoReg(multipartFile, username));
    }

    @PostMapping("/bid")
    public ResponseEntity<Long> toBid(@RequestBody BidDTO bidDTO) {
        return ResponseEntity.ok(masterService.toBid(bidDTO));
    }

    @GetMapping("/{id}/photo")
    public ResponseEntity<InputStreamResource> getPhoto(@PathVariable Long id) throws MalformedURLException, FileNotFoundException {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("image/jpeg"))
                .body(new InputStreamResource(masterService.getPhoto(id)));
    }


    @GetMapping("/photo/key")
    public ResponseEntity<InputStreamResource> getPhoto(@RequestParam String key) throws MalformedURLException, FileNotFoundException {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("image/jpeg"))
                .body(new InputStreamResource(masterService.getPhoto(key)));
    }

    @GetMapping("/top10")
    public ResponseEntity<List<MasterDTO>> getTop10MastersByRate() {
        List<MasterDTO> topMasters = masterService.getTop10MastersByRate();
        return ResponseEntity.ok(topMasters);
    }


    @GetMapping("/{id}/feedbacks")
    public ResponseEntity<List<GetFeedbackResponse>> getFeedbacks(@PathVariable Long id) {
        return ResponseEntity.ok(masterService.getFeedbacks(id));
    }

    @Operation(description = "Delete Master")
    @DeleteMapping("/")
    public ResponseEntity<Long> deleteMaster(@RequestParam String username) throws IOException {
        return ResponseEntity.ok(masterService.deleteMaster(username));
    }
}
