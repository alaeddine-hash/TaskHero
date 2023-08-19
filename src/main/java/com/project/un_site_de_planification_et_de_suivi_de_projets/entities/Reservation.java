package com.project.un_site_de_planification_et_de_suivi_de_projets.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.jshell.Snippet;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservations")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description ;

    private LocalDate startDate ;

    private LocalDate cancelDate ;

    private  LocalDate completeDate ;

    private  ReservationStatus status ;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="client_id", nullable=false)
    private User client;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="id_availability", nullable=false)
    private Availability availability;
}
