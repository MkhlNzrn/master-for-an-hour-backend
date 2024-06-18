package org.example.entities;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String url;

    @ManyToOne
    @JoinColumn(name = "master_id")
    private Master master;

    @ManyToOne
    @JoinColumn(name = "master_access_request_id")
    private MasterAccessRequest masterAccessRequest;

    public Document(String name, String url, Master master) {
        this.name = name;
        this.url = url;
        this.master = master;
    }

    public Document(String name, String url, MasterAccessRequest masterAccessRequest) {
        this.name = name;
        this.url = url;
        this.masterAccessRequest = masterAccessRequest;
    }
}
