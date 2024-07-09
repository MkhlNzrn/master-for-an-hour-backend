package org.example.services;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Service
public interface EmailService {

    void sendSimpleEmail(String toAddress, String subject, String message);

    void sendEmailWithAttachment(String toAddress, String subject, String message, String attachment) throws MessagingException, FileNotFoundException, FileNotFoundException;
}
