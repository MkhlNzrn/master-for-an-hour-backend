package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entities.User;
import org.example.exceptions.InvalidRoleException;
import org.example.exceptions.UserEmailAlreadyExistsException;
import org.example.pojo.JwtAuthenticationResponse;
import org.example.pojo.SignInRequest;
import org.example.pojo.SignUpRequest;
import org.example.enums.ERole;
import org.example.services.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ClientService clientService;
    private final MasterService masterService;


    public JwtAuthenticationResponse signUp(SignUpRequest request) throws IOException {

        if (userService.userExists(request.getEmail())) throw new UserEmailAlreadyExistsException(request.getEmail());

        User user = User.builder()
                .username(request.getEmail())
                .firstName(request.getFirstName())
                .password(passwordEncoder.encode(request
                        .getPassword())).role(ERole.valueOf(request.getRole()))
                .build();

        userService.create(user);

        if (request.getRole().equals(ERole.ROLE_CLIENT.name())) {
            clientService.createClient(request, user);
        } else if (request.getRole().equals(ERole.ROLE_MASTER.name())) {
            masterService.createMasterAccountRequest(request, user);
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
                    .loadUserByUsername(request.getEmail());

            var jwt = jwtService.generateToken(user);
            return new JwtAuthenticationResponse(jwt);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Bad credentials");
        }
    }
}