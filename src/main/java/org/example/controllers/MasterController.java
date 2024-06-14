package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.MasterDTO;
import org.example.pojo.MasterInfoDTO;
import org.example.services.MasterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/masters")
@RequiredArgsConstructor
@Tag(name = "Operations with Masters")
public class MasterController {

    private final MasterService masterService;

    @Operation(description = "Get a full information about Master by ID")
    @GetMapping("/{id}")
    public ResponseEntity<MasterDTO> getMaster(@PathVariable Long id) {
        return ResponseEntity.ok(masterService.getMaster(id));
    };

    @Operation(description = "Get an information for client about Master by ID")
    @GetMapping("/info/{id}")
    public ResponseEntity<MasterInfoDTO> getMasterInfo(@PathVariable Long id) {
        return ResponseEntity.ok(masterService.getMasterInfo(id));
    };

    @Operation(description = "Get an information about all Masters")
    @GetMapping("/")
    public ResponseEntity<Page<MasterDTO>> getAllMasters(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.ok(masterService.getAllMasters(pageRequest));
    };
}
