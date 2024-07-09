package org.example.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.entities.Task;

import java.util.Date;

@Data
@Builder
@Getter
@Setter
public class BidDTO {
    private Date dateStart;
    private Date dateEnd;
    private Long price;
    private Long taskId;
    private Long userId;
}
