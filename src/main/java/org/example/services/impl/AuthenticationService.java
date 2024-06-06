package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entities.User;
import org.example.pojo.JwtAuthenticationResponse;
import org.example.pojo.SignInRequest;
import org.example.pojo.SignUpRequest;
import org.example.enums.ERole;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserServiceImpl userService;
    private final JwtServiceImpl jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request
                .getPassword())).role(ERole.ROLE_CLIENT)
                .build();

        userService.create(user);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }


    public JwtAuthenticationResponse signIn(SignInRequest request) {
        try {
                var user = userService
                    .userDetailsService()
                    .loadUserByUsername(request.getUsername());

            var jwt = jwtService.generateToken(user);
            return new JwtAuthenticationResponse(jwt);
    } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Bad credentials");
        }
        }
}