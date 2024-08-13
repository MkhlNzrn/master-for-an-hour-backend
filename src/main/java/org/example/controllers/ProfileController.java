package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.ChangeProfileDTO;
import org.example.pojo.ProfileDTO;
import org.example.services.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
@Tag(name = "Operations with Profiles")
public class ProfileController {

    private final ProfileService profileService;

    @Operation(description = "Get Master or Client profile info")
    @GetMapping("/{id}")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.getProfileInfo(id));
    }

    @PatchMapping("/{id}/change")
    public ResponseEntity<Long> changeProfile(@PathVariable Long id, @RequestBody ChangeProfileDTO changeProfileDTO) {
        return ResponseEntity.ok(profileService.changeProfile(id, changeProfileDTO));
    }

}
