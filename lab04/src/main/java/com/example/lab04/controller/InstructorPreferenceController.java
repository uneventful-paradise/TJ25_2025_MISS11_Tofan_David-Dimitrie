package com.example.lab04.controller;

import com.example.lab04.dto.InstructorPreferenceRequestDto;
import com.example.lab04.dto.InstructorPreferenceResponseDto;
import com.example.lab04.service.InstructorPreferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructor-preferences")
@Tag(name = "Instructor Preferences", description = "Manage weighting of compulsory courses for optional course selection")
public class InstructorPreferenceController {

    private final InstructorPreferenceService service;

    public InstructorPreferenceController(InstructorPreferenceService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    // Only Instructors or Admins should set these preferences
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Set a weight preference", description = "Defines how important a compulsory course grade is for admission to an optional course.")
    public InstructorPreferenceResponseDto createPreference(@Valid @RequestBody InstructorPreferenceRequestDto dto) {
        return service.savePreference(dto);
    }

    @GetMapping("/course/{optionalCourseId}")
    @Operation(summary = "Get weights for an optional course", description = "Returns all compulsory course weights defined for a specific optional course.")
    public List<InstructorPreferenceResponseDto> getPreferences(@PathVariable Long optionalCourseId) {
        return service.getPreferencesForOptionalCourse(optionalCourseId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @Operation(summary = "Delete a preference")
    public void deletePreference(@PathVariable Long id) {
        service.deletePreference(id);
    }
}