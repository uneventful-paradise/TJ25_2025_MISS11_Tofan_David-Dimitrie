package com.example.lab04.service;

import com.example.lab04.Pack;
import com.example.lab04.dto.PackResponseDto;
import com.example.lab04.repository.PackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PackService {
    private final PackRepository packRepository;

    public PackService(PackRepository packRepository) {
        this.packRepository = packRepository;
    }

    @Transactional
    public PackResponseDto save(Pack pack) {
        Pack savedPack = packRepository.save(pack);
        return mapToResponseDto(savedPack);
    }

    /**
     * NEW method to find all
     */
    @Transactional(readOnly = true)
    public List<PackResponseDto> findAll() {
        return packRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // --- Helper DTO Mapper ---
    private PackResponseDto mapToResponseDto(Pack pack) {
        PackResponseDto dto = new PackResponseDto();
        dto.setId(pack.getId());
        dto.setName(pack.getName());
        dto.setYear(pack.getYear());
        dto.setSemester(pack.getSemester());
        return dto;
    }

    public void deleteAll() {
        packRepository.deleteAll();
    }
}
