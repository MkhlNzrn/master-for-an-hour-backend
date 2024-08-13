package org.example.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.entities.Client;
import org.example.entities.Master;
import org.example.exceptions.NoMasterAccessRequestsException;
import org.example.pojo.ChangeProfileDTO;
import org.example.pojo.MasterDTO;
import org.example.pojo.TaskDTO;
import org.example.pojo.UpdateTaskDTO;
import org.example.repositories.MasterRepository;
import org.example.services.ClientService;
import org.example.services.MasterService;
import org.example.services.ProfileService;
import org.example.services.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final TaskService taskService;
    private final ProfileService profileService;
    private final MasterRepository masterRepository;
    private final ClientService clientService;

    @Operation(description = "Show all Master account access requests")
    @GetMapping("/access-requests")
    public ResponseEntity<List<Master>> masterAccessRequests() {
        List<Master> masterAccessRequests = masterRepository.findAllByIsAcceptedFalse();
        if (masterAccessRequests.isEmpty()) {
            throw new NoMasterAccessRequestsException();
        }
        return ResponseEntity.ok(masterAccessRequests);
    }

    @Operation(description = "Get an information about all Masters")
    @GetMapping("/masters")
    public ResponseEntity<Page<MasterDTO>> getAllMasters(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(masterService.getAllMasters(pageRequest));
    }

    @Operation(description = "Get an information about all non verified Masters")
    @GetMapping("/non-verified-masters")
    public ResponseEntity<Page<MasterDTO>> getAllNonVerifiedMasters(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(masterService.getAllNonVerifiedMasters(pageRequest));
    }

    @Operation(description = "Accept Master account access request by Id")
    @PostMapping("/verify/{id}")
    public ResponseEntity<Long> verifyDocks(@PathVariable Long id) {
        return ResponseEntity.ok(masterService.verifyDocks(id));
    }

    @Operation(description = "Get an information about all Clients")
    @GetMapping("/clients")
    public ResponseEntity<Page<Client>> getAllClients(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(clientService.getAllClients(pageRequest));
    }

    @Operation(description = "Get an information about all Tasks")
    @GetMapping("/tasks")
    public ResponseEntity<Page<TaskDTO>> getAllTasks(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(taskService.getTasks(pageRequest));
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

    @PatchMapping("/profile/{id}/change")
    public ResponseEntity<Long> changeProfile(@PathVariable Long id, @RequestBody ChangeProfileDTO changeProfileDTO) {

        return ResponseEntity.ok(profileService.changeProfile(id, changeProfileDTO));
    }

    @Operation(description = "Update Task")
    @PatchMapping("/task-update")
    public ResponseEntity<Long> updateTask(@RequestBody UpdateTaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.updateTask(taskDTO));
    }
}
