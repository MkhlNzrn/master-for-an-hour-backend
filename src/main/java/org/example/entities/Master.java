package org.example.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "masters")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Master {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "telegram_tag")
    private String telegramTag;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "age")
    private byte age;

    @Column(name = "rate", nullable = false)
    private float rate;

    @Column(name = "photo_link")
    private String photoLink;

    @Column(name = "user_id")
    private Long user_id;

    public Master(String fullName, String email, String phoneNumber, String telegramTag, String description, byte age, float rate, String photoLink, Long user_id) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.telegramTag = telegramTag;
        this.description = description;
        this.age = age;
        this.rate = rate;
        this.photoLink = photoLink;
        this.user_id = user_id;
    }
}
