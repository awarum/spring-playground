package com.nwboxed.simplespring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    public Car(String type, String colour) {
        this.type = type;
        this.colour = colour;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PUBLIC)
    private String id;
    private String type;
    private String colour;
}
