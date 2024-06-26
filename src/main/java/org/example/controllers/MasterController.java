package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entities.Master;
import org.example.exceptions.NoMasterAccessRequestsException;
import org.example.pojo.MasterDTO;
import org.example.pojo.MasterInfoDTO;
import org.example.repositories.MasterRepository;
import org.example.services.MasterService;
import org.example.wrappers.PathSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/masters")
@RequiredArgsConstructor
@Tag(name = "Operations with Masters")
public class MasterController {

    private final MasterService masterService;
    private final MasterRepository masterRepository;

    @Operation(description = "Get a full information about Master by ID")
    @GetMapping("/{id}")
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

    @PostMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile multipartFile, @RequestParam String username) throws IOException {
        return ResponseEntity.ok(masterService.uploadPhoto(multipartFile, username));
    }

    @Operation(description = "Show all Master account access requests")
    @GetMapping("/access-requests")
    public ResponseEntity<List<Master>> masterAccessRequests() {
        List<Master> masterAccessRequests = masterRepository.findAllByIsAcceptedFalse();
        if (masterAccessRequests.isEmpty()) {
            throw new NoMasterAccessRequestsException();
        }
        return ResponseEntity.ok(masterAccessRequests);
    }

    @Operation(description = "Accept Master account access request by Id")
    @PostMapping("/accept/{id}")
    public ResponseEntity<Long> acceptMasterAccessRequest(@PathVariable Long id) {
        return ResponseEntity.ok(masterService.acceptMasterAccessRequest(id));
    }

    @Operation(description = "Discard Master account access request by Id")
    @DeleteMapping("/discard/{id}")
    public ResponseEntity<Long> discardMasterAccessRequest(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(masterService.discardMasterAccessRequest(id));
    }

    @Operation(description = "Delete Master")
    @DeleteMapping("/")
    public ResponseEntity<Long> deleteMaster(@RequestParam String username) throws IOException {
        return ResponseEntity.ok(masterService.deleteMaster(username));
    }
}
