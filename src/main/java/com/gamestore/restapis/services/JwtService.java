package com.gamestore.restapis.services;

import com.gamestore.restapis.entities.Role;
import com.gamestore.restapis.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class JwtService {
    private final JwtEncoder jwtEncoder;

    public String generateToken(User user) {
        var now = Instant.now();
        var role = user.getRole() == null ? Role.USER : user.getRole();
        var claims = JwtClaimsSet.builder()
                .issuer("game-store")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .claim("roles", List.of(role.name()))
                .build();
        var headers = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(headers, claims)).getTokenValue();
    }
}
