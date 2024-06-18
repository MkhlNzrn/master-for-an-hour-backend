package org.example.pojo;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DocumentDTO {
    private String url;
}
