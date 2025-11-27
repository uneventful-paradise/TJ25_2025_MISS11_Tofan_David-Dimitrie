package com.example.lab04.controller;

import com.example.lab04.dto.solver.SolverResponseDto;
import com.example.lab04.service.MatchingClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/matching")
public class MatchingTriggerController {

    private final MatchingClientService matchingClientService;

    public MatchingTriggerController(MatchingClientService matchingClientService) {
        this.matchingClientService = matchingClientService;
    }

    @PostMapping("/execute/{packId}")
    public CompletableFuture<ResponseEntity<List<SolverResponseDto>>> runMatching(@PathVariable Long packId) {
        return matchingClientService.getMatches(packId)
                .thenApply(results -> {
                    System.out.println("MATCHING RESULTS: " + results);
                    return ResponseEntity.ok(results);
                });
    }
}