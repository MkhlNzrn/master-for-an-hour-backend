package org.example.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendFeedbackDTO {
    private Long taskId;
    private Float rate;
    private String feedback;
}
