package org.example.controllers;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entities.Bid;
import org.example.services.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Tag(name = "Operations with Bids")
public class ClientController {

    private final ClientService clientService;

    @DeleteMapping("/bid/{id}")
    public ResponseEntity<Long> chooseBid(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.chooseBid(id));
    }

    @GetMapping("/bids/task/{id}")
    public ResponseEntity<List<Bid>> getAllBidsByTaskId(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getAllBids(id));
    }

}
