package com.gamestore.restapis.controllers;

import com.gamestore.restapis.dtos.ProfileDto;
import com.gamestore.restapis.mappers.ProfileMapper;
import com.gamestore.restapis.repositories.ProfileRepository;
import com.gamestore.restapis.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.stream.StreamSupport;

@RestController
@AllArgsConstructor
@RequestMapping("/api/profiles")
public class ProfileController {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;

    @GetMapping
    public Iterable<ProfileDto> getAllProfiles() {
        return StreamSupport.stream(profileRepository.findAll().spliterator(), false)
                .map(profileMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable Long id) {
        var profile = profileRepository.findById(id).orElse(null);
        if (profile == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(profileMapper.toDto(profile));
    }

    @PostMapping
    public ResponseEntity<ProfileDto> createProfile(
            @RequestBody ProfileDto profileDto,
            UriComponentsBuilder uriBuilder
    ) {
        if (profileDto.getUserId() == null || !userRepository.existsById(profileDto.getUserId())) {
            return ResponseEntity.badRequest().build();
        }

        if (profileRepository.existsById(profileDto.getUserId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        var profile = profileMapper.toEntity(profileDto);
        profileRepository.save(profile);

        var savedProfileDto = profileMapper.toDto(profile);
        var uri = uriBuilder.path("/api/profiles/{id}").buildAndExpand(savedProfileDto.getId()).toUri();

        return ResponseEntity.created(uri).body(savedProfileDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileDto> updateProfile(
            @PathVariable Long id,
            @RequestBody ProfileDto profileDto
    ) {
        var profile = profileRepository.findById(id).orElse(null);
        if (profile == null) {
            return ResponseEntity.notFound().build();
        }

        if (profileDto.getUserId() == null || !profileDto.getUserId().equals(id)) {
            return ResponseEntity.badRequest().build();
        }

        profileMapper.update(profileDto, profile);
        profileRepository.save(profile);

        return ResponseEntity.ok(profileMapper.toDto(profile));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        var profile = profileRepository.findById(id).orElse(null);
        if (profile == null) {
            return ResponseEntity.notFound().build();
        }

        profileRepository.delete(profile);
        return ResponseEntity.noContent().build();
    }
}
