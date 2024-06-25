package org.example.pojo;

import lombok.Builder;
import lombok.Data;
import org.example.entities.Document;

import java.util.List;

@Data
@Builder
public class MasterDTO {

    private Long id;

    private String firstName;

    private String middleName;

    private String lastName;

    private String metroStation;

    private String email;

    private String phoneNumber;

    private String telegramTag;

    private String description;

    private byte age;

    private float rate;

    private String photoLink;

    private Boolean isAccepted;

    private List<Document> documents;

    private Long userId;
}
