package com.gamestore.restapis.controllers;

import com.gamestore.restapis.dtos.ChangePasswordRequest;
import com.gamestore.restapis.dtos.UpdateUserRequest;
import com.gamestore.restapis.dtos.UserDto;
import com.gamestore.restapis.mappers.UserMapper;
import com.gamestore.restapis.repositories.UserRepository;
import com.gamestore.restapis.services.CurrentUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/me")
public class MeController {
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public UserDto getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        return userMapper.toDto(currentUserService.getCurrentUser(jwt));
    }

    @PutMapping
    public UserDto updateCurrentUser(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody UpdateUserRequest request
    ) {
        var user = currentUserService.getCurrentUser(jwt);
        userMapper.update(request, user);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changeCurrentUserPassword(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody ChangePasswordRequest request
    ) {
        var user = currentUserService.getCurrentUser(jwt);

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        var user = currentUserService.getCurrentUser(jwt);
        userRepository.delete(user);

        return ResponseEntity.noContent().build();
    }
}
