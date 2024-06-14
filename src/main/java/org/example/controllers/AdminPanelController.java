package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.entities.MasterAccessRequest;
import org.example.exceptions.NoMasterAccessRequestsException;
import org.example.repositories.MasterAccessRequestRepository;
import org.example.services.AdminPanelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Administration Panel")
public class AdminPanelController {

    private final MasterAccessRequestRepository masterAccessRequestRepository;
    private final AdminPanelService adminPanelService;

    @Operation(description = "Show all Master account access requests")
    @GetMapping("/access-requests")
    public ResponseEntity<List<MasterAccessRequest>> masterAccessRequests() {
        List<MasterAccessRequest> masterAccessRequests = masterAccessRequestRepository.findAll();
        if (masterAccessRequests.isEmpty()) {
            throw new NoMasterAccessRequestsException();
        }
        return ResponseEntity.ok(masterAccessRequests);
    }

    @Operation(description = "Accept Master account access request by Id")
    @PostMapping("/accept/{id}")
    public ResponseEntity<Long> acceptMasterAccessRequest(@PathVariable Long id) {
        return ResponseEntity.ok(adminPanelService.acceptMasterAccessRequest(id));
    }

    @Operation(description = "Discard Master account access request by Id")
    @DeleteMapping("/discard/{id}")
    public ResponseEntity<Long> discardMasterAccessRequest(@PathVariable Long id) {
        return ResponseEntity.ok(adminPanelService.discardMasterAccessRequest(id));
    }
}
