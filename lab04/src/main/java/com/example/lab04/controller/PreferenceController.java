package com.example.lab04.controller;

import com.example.lab04.Preference;
import com.example.lab04.dto.PreferenceRequestDto;
import com.example.lab04.dto.PreferenceResponseDto;
import com.example.lab04.service.PreferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestController
@RequestMapping("/api/preferences")
@Tag(name = "Student Preferences", description = "API for managing student course preferences")
public class PreferenceController {

    private final PreferenceService preferenceService;

    public PreferenceController(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @Operation(summary = "Create a new preference",
            description = "Submits a student's preference (rank) for a specific course. " +
                    "The course must be in the student's pack.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Preference created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input (validation error)"),
                    @ApiResponse(responseCode = "404", description = "Student or Course not found")
            })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PreferenceResponseDto createPreference(@Valid @RequestBody PreferenceRequestDto dto) {
        return preferenceService.createPreference(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PreferenceResponseDto> getPreferenceById(
            @PathVariable Long id, WebRequest request) {
        Preference preference = preferenceService.getPreferenceEntityById(id);
        String etag = "\"" + preference.getId() + "-" + preference.getRank() + "\"";
        if (request.checkNotModified(etag)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).eTag(etag).build();
        }
        PreferenceResponseDto dto = preferenceService.getPreferenceById(id);
        return ResponseEntity.ok().eTag(etag).body(dto);
    }

    @GetMapping
    public List<PreferenceResponseDto> getAllPreferences() {
        return preferenceService.findAll();
    }

    @GetMapping("/student/{studentId}")
    public List<PreferenceResponseDto> getPreferencesByStudent(@PathVariable Long studentId) {
        return preferenceService.getPreferencesByStudent(studentId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePreference(@PathVariable Long id) {
        preferenceService.deletePreference(id);
    }
}