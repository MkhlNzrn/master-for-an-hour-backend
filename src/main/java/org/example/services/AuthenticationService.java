package org.example.services;

import org.example.pojo.JwtAuthenticationResponse;
import org.example.pojo.SignInRequest;
import org.example.pojo.SignUpRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface AuthenticationService {

    JwtAuthenticationResponse signUp(SignUpRequest request) throws IOException;

    JwtAuthenticationResponse signIn(SignInRequest request);
}
