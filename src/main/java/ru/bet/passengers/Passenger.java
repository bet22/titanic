package ru.bet.passengers;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "passengers")
public class Passenger {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private PassengerClass pclass;

    private Boolean survived;

    @Column(name = "name")
    private String name;

    private String sex;

    private double age;

    @Column(name = "siblings_spouses")
    private int siblingsSpouses;

    @Column(name = "parents_children")
    private int parentsChildren;

    @Column(name = "fare")
    private BigDecimal fare;

}
