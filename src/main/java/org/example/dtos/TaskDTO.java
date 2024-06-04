package org.example.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskDTO {

    private Long id;

    private String name;

    private Long categoryId;

    private String categoryName;
}
