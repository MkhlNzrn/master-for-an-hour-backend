package org.example.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.entities.Master;
import org.example.exceptions.NoMasterAccessRequestsException;
import org.example.repositories.MasterRepository;
import org.example.services.MasterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Panel")
public class AdminController {

    private final MasterService masterService;
    private final MasterRepository masterRepository;

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

    @Operation(description = "Accept Master account access request by Id")
    @PostMapping("/verify/{id}")
    public ResponseEntity<Long> verifyDocks(@PathVariable Long id) {
        return ResponseEntity.ok(masterService.verifyDocks(id));
    }

    @Operation(description = "Discard Master account access request by Id")
    @DeleteMapping("/discard/{id}")
    public ResponseEntity<Long> discardMasterAccessRequest(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(masterService.discardMasterAccessRequest(id));
    }
}
