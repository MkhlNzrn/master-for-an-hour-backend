package org.example.pojo;

import lombok.Builder;
import lombok.Data;
import org.example.entities.Document;

import java.util.List;

@Data
@Builder
public class MasterInfoDTO {

    private Long id;

    private String firstName;

    private String middleName;

    private String lastName;

    private List<String> metroStation;

    private String description;

    private byte age;

    private float rate;

    private String photoLink;

    private List<String> categories;

    private List<Document> documents;

}
