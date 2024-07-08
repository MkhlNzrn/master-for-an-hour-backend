package org.example.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @ManyToOne
    private User user;

    @ManyToOne
    private Category category;

    public Task(String description, Category category, Date startDate, Date endDate, User user) {
        this.description = description;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
    }
}
