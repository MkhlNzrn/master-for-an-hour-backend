package org.example.controllers;

import org.example.dtos.MasterDTO;
import org.example.dtos.MasterInfoDTO;
import org.example.repositories.MasterRepository;
import org.example.services.MasterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/masters")
public class MasterController {

    private final MasterService masterService;

    public MasterController(MasterService masterService) {
        this.masterService = masterService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MasterDTO> getMaster(@PathVariable Long id) {
        return ResponseEntity.ok(masterService.getMaster(id));
    };

    /*@PreAuthorize()*/
    @GetMapping("/info/{id}")
    public ResponseEntity<MasterInfoDTO> getMasterInfo(@PathVariable Long id) {
        return ResponseEntity.ok(masterService.getMasterInfo(id));
    };

    @GetMapping("/")
    public ResponseEntity<Page<MasterDTO>> getAllMasters(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(masterService.getAllMasters(pageRequest));
    };
}
