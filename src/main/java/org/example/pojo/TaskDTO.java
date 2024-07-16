package org.example.pojo;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class TaskDTO {

    @NotBlank(message = "Идентификатор услуги не может быть пустым")
    private Long id;

    private String description;

    private Date startDate;

    private Date endDate;

    private Boolean isCompleted;

    private Long categoryId;

    private String categoryName;

    private Long userId;

    private String userName;

    private Long masterId;

    private String masterName;

    private String masterEmail;

    private String masterPhoneNumber;

    private String masterTelegramTag;

    private String clientEmail;

    private String clientPhoneNumber;

    private String clientTelegramTag;
}
