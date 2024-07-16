package org.example.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "masters")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Master {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name", nullable = false)
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "metro_station", nullable = false)
    private String metroStation;

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

    @Column(name = "is_accepted")
    private Boolean isAccepted;

    @ManyToMany
    private List<Category> categories;

    @Column(name = "is_verified_by_docks")
    private Boolean isVerifiedByDocks = false;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Master(String firstName, List<Category> categories,  String middleName, String lastName, String email, Boolean isAccepted, String metroStation, String phoneNumber, String telegramTag, String description, byte age, float rate, String photoLink, User user) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.isAccepted = isAccepted;
        this.metroStation = metroStation;
        this.phoneNumber = phoneNumber;
        this.telegramTag = telegramTag;
        this.description = description;
        this.categories = categories;
        this.age = age;
        this.rate = rate;
        this.photoLink = photoLink;
        this.user = user;
    }


    public Master(String email, String phoneNumber, String telegramTag) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.telegramTag = telegramTag;
    }
}
