package com.example.stablematch.controller;

import com.example.stablematch.dto.MatchingRequestDto;
import com.example.stablematch.dto.MatchingResponseDto;
import com.example.stablematch.service.MatchingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solver")
public class MatchingController {

    private final MatchingService matchingService;

    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @PostMapping("/match")
    public ResponseEntity<List<MatchingResponseDto>> solveProblem(@RequestBody MatchingRequestDto request) {
        if (request.getStudents() == null || request.getCourses() == null) {
            return ResponseEntity.badRequest().build();
        }

        List<MatchingResponseDto> solution = matchingService.solveMatching(request);
        return ResponseEntity.ok(solution);
    }

    @PostMapping("/match/random")
    public ResponseEntity<List<MatchingResponseDto>> solveRandom(@RequestBody MatchingRequestDto request) {
        System.out.println("StableMatch received request for " + request.getStudents().size() + " students.");
        return ResponseEntity.ok(matchingService.solveRandomMatching(request));
    }
}