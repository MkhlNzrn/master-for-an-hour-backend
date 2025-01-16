package org.example.pojo;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.example.entities.User;

@Data
@Builder
public class ClientDTO {

    private Long id;

    private String name;

    private String email;

    private String phoneNumber;

    private String telegramTag;

    private User userId;

}
