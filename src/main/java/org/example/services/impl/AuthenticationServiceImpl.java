package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entities.User;
import org.example.exceptions.InvalidRoleException;
import org.example.pojo.JwtAuthenticationResponse;
import org.example.pojo.SignInRequest;
import org.example.pojo.SignUpRequest;
import org.example.enums.ERole;
import org.example.services.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ClientService clientService;
    private final AdminPanelService adminPanelService;


    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        if (request.getRole().equals(ERole.ROLE_MASTER.name())) {
            adminPanelService.createMasterAccountRequest(request);
            return new JwtAuthenticationResponse(
                    "the application for creating a master account has been accepted, the result of consideration by the administrator is expected"
            );
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request
                        .getPassword())).role(ERole.valueOf(request.getRole()))
                .build();

        userService.create(user);

        if (request.getRole().equals(ERole.ROLE_CLIENT.name())) {
            clientService.createClient(request, user.getId());
        } else {
            throw new InvalidRoleException(request.getRole());
        }

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }


    public JwtAuthenticationResponse signIn(SignInRequest request) {
        try {
            UserDetails user = userService
                    .userDetailsService()
                    .loadUserByUsername(request.getUsername());

            var jwt = jwtService.generateToken(user);
            return new JwtAuthenticationResponse(jwt);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Bad credentials");
        }
    }
}