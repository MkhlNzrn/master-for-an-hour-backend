package org.example.pojo;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UpdateTaskDTO {

    @NotBlank(message = "Идентификатор услуги не может быть пустым")
    private Long id;

    private String description;

    private Date startDate;

    private Date endDate;

    private Boolean isCompleted;

    private String categoryName;
}
