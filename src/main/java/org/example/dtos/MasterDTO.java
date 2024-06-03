package org.example.dtos;

import lombok.Builder;
import lombok.Data;
import org.example.entites.Document;

import java.util.List;

@Data
@Builder
public class MasterDTO {

    private Long id;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String telegramTag;

    private String description;

    private byte age;

    private float rate;

    private String photoLink;

    private List<Document> documents;
}
