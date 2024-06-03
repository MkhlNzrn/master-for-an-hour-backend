package org.example.entites;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Document {
    @Id
    private Long id;

    private String name;

    private String url;

    @ManyToOne
    @JoinColumn(name = "master_id")
    private Master master;
}
