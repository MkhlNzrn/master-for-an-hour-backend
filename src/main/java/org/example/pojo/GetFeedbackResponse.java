package org.example.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetFeedbackResponse {
    private String feedback;
    private Float rate;
}
