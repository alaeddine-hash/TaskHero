package com.project.un_site_de_planification_et_de_suivi_de_projets.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ratings")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private  long id ;

    private String description ;

    private LocalDate rateDate;

    private UserRating userRating ;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="client_id", nullable=false)
    private User client;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="provider_id", nullable=false)
    private User provider;
}
