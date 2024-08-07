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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "rate")
    private Float rate;

    @Column(name = "feedback")
    private String feedback;

    @Column(name = "price")
    private Long price;

    @Column(name = "max_price")
    private Long maxPrice;

    @ManyToOne
    private User client;

    @ManyToOne
    private User master;

    @ManyToOne
    private Category category;

    public Task(String description, Category category, Date startDate, Date endDate, User client, Long maxPrice) {
        this.description = description;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.client = client;
        this.isCompleted = false;
        this.rate = null;
        this.feedback = null;
        this.price = null;
        this.maxPrice = maxPrice;
    }
}
