package org.example.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "telegram_tag")
    private String telegramTag;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Client(String name, String email, String phoneNumber, String telegramTag, User user) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.telegramTag = telegramTag;
        this.user = user;
    }
}
