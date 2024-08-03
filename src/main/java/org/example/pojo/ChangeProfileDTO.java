package org.example.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChangeProfileDTO {

    private List<String> metroStation;

    private String email;

    private String phoneNumber;

    private String telegramTag;

    private String description;

    private List<String> categories;

}
