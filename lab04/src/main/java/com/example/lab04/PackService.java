package com.example.lab04;

import org.springframework.stereotype.Service;

@Service
public class PackService {
    private final PackRepository packRepository;

    public PackService(PackRepository packRepository) {
        this.packRepository = packRepository;
    }

    public Pack save(Pack pack) {
        return packRepository.save(pack);
    }

    public void deleteAll() {
        packRepository.deleteAll();
    }
}
