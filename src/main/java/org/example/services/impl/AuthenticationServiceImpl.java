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
import org.example.repositories.UserRepository;
import org.example.services.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final UserRepository userRepository;


    public JwtAuthenticationResponse signUp(SignUpRequest request) throws IOException {

        if (userService.userExists(request.getEmail())) throw new UserEmailAlreadyExistsException(request.getEmail());

        User user = User.builder()
                .username(request.getEmail())
                .firstName(request.getFirstName())
                .password(passwordEncoder.encode(request
                        .getPassword())).role(ERole.valueOf(request.getRole()))
                .build();

        userService.create(user);

        Long entityId;
        if (request.getRole().equals(ERole.ROLE_CLIENT.name())) {
            entityId = clientService.createClient(request, user);
        } else if (request.getRole().equals(ERole.ROLE_MASTER.name())) {
            entityId = masterService.createMasterAccountRequest(request, user);
        } else {
            throw new InvalidRoleException(request.getRole());
        }

        var jwt = jwtService.generateToken(user, entityId);
        return new JwtAuthenticationResponse(jwt);
    }


    public JwtAuthenticationResponse signIn(SignInRequest request) {
        try {
            User user = userRepository.findByUsername(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not fount by username: " + request.getEmail()));
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            UserDetails userDetails = userService
                    .userDetailsService()
                    .loadUserByUsername(request.getEmail());

                Long entityId = null;
                if (userDetails instanceof User customUserDetails) {
                    if (customUserDetails.getRole() == ERole.ROLE_CLIENT) {
                        entityId = clientService.getClientByUserUsername(userDetails.getUsername());
                    } else if (customUserDetails.getRole().equals(ERole.ROLE_MASTER)) {
                        entityId = masterService.getMasterByUserUsername(userDetails.getUsername());
                    }
                }
                var jwt = jwtService.generateToken(userDetails, entityId);
                return new JwtAuthenticationResponse(jwt);
            } else {
                throw new BadCredentialsException("Bad credentials");
            }
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Bad credentials");
        }
    }
}