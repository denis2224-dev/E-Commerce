package com.gamestore.restapis.controllers;

import com.gamestore.restapis.dtos.AddressDto;
import com.gamestore.restapis.mappers.AddressMapper;
import com.gamestore.restapis.repositories.AddressRepository;
import com.gamestore.restapis.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.stream.StreamSupport;

@RestController
@AllArgsConstructor
@RequestMapping("/api/addresses")
public class AddressController {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    @GetMapping
    public Iterable<AddressDto> getAllAddresses() {
        return StreamSupport.stream(addressRepository.findAll().spliterator(), false)
                .map(addressMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddress(@PathVariable Long id) {
        var address = addressRepository.findById(id).orElse(null);
        if (address == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(addressMapper.toDto(address));
    }

    @PostMapping
    public ResponseEntity<AddressDto> createAddress(
            @RequestBody AddressDto addressDto,
            UriComponentsBuilder uriBuilder
    ) {
        if (addressDto.getUserId() == null || !userRepository.existsById(addressDto.getUserId())) {
            return ResponseEntity.badRequest().build();
        }

        var address = addressMapper.toEntity(addressDto);
        addressRepository.save(address);

        var savedAddressDto = addressMapper.toDto(address);
        var uri = uriBuilder.path("/api/addresses/{id}").buildAndExpand(savedAddressDto.getId()).toUri();

        return ResponseEntity.created(uri).body(savedAddressDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddress(
            @PathVariable Long id,
            @RequestBody AddressDto addressDto
    ) {
        var address = addressRepository.findById(id).orElse(null);
        if (address == null) {
            return ResponseEntity.notFound().build();
        }

        if (addressDto.getUserId() == null || !userRepository.existsById(addressDto.getUserId())) {
            return ResponseEntity.badRequest().build();
        }

        addressMapper.update(addressDto, address);
        addressRepository.save(address);

        return ResponseEntity.ok(addressMapper.toDto(address));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        var address = addressRepository.findById(id).orElse(null);
        if (address == null) {
            return ResponseEntity.notFound().build();
        }

        addressRepository.delete(address);
        return ResponseEntity.noContent().build();
    }
}
