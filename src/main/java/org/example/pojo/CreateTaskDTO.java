package org.example.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateTaskDTO {

    private String name;

    private String categoryName;
}
