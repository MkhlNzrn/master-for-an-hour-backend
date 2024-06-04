package org.example.entites;

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
    @GeneratedValue
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

    @OneToMany(mappedBy = "master")
    private List<Document> documents;
}
