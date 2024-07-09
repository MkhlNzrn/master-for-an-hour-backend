package org.example.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "emails_pins")
@Getter
@Setter
@NoArgsConstructor
public class EmailPin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "pin")
    private int pin;

    public EmailPin(String email, int pin) {
        this.email = email;
        this.pin = pin;
    }

    public EmailPin setPin(int pin) {
        this.pin = pin;
        return this;
    }

}
