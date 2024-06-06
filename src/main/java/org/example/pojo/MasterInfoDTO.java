package org.example.pojo;

import lombok.Builder;
import lombok.Data;
import org.example.entities.Document;

import java.util.List;

@Data
@Builder
public class MasterInfoDTO {

    private String fullName;

    private String description;

    private byte age;

    private float rate;

    private String photoLink;

    private List<Document> documents;
}
