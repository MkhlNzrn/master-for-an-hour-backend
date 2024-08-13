package org.example.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "bids")
@Getter
@Setter
@NoArgsConstructor
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_start")
    private Date dateStart;

    @Column(name = "date_end")
    private Date dateEnd;

    @Column(name = "price")
    private Long price;

    @ManyToOne
    private Task task;

    @ManyToOne
    private Master master;


    public Bid(Date dateStart, Date dateEnd, Long price, Task task, Master master) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.price = price;
        this.task = task;
        this.master = master;
    }
}
