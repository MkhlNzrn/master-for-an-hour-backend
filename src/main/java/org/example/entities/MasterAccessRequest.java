package org.example.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "master_access_requests")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class MasterAccessRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name", nullable = false)
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "telegram_tag")
    private String telegramTag;

    @Column(name = "description")
    private String description;

    @Column(name = "age")
    private byte age;

    @Column(name = "rate")
    private float rate;

    @Column(name = "photo_link")
    private String photoLink;

    public MasterAccessRequest(String firstName, String middleName, String lastName, String email, String password, String role, String phoneNumber, String telegramTag, String description, byte age, float rate, String photoLink) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.telegramTag = telegramTag;
        this.description = description;
        this.age = age;
        this.rate = rate;
        this.photoLink = photoLink;
    }
}
