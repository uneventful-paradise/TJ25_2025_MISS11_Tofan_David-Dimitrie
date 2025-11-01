package com.example.lab04.controller;

import com.example.lab04.Pack;
import com.example.lab04.dto.PackResponseDto;
import com.example.lab04.service.PackService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packs")
public class PackController {

    private final PackService packService;

    public PackController(PackService packService) {
        this.packService = packService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PackResponseDto createPack(@RequestBody Pack pack) {
        return packService.save(pack);
    }

    /**
     * NEW method to get all
     */
    @GetMapping
    public List<PackResponseDto> getAllPacks() {
        return packService.findAll();
    }
}