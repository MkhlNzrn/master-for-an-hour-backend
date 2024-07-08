package org.example.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CreateTaskDTO {

    private String description;

    private Date startDate;

    private Date endDate;

    private String categoryName;
}
