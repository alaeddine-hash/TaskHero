package com.project.un_site_de_planification_et_de_suivi_de_projets.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "favorites")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate addDate;

    private int note ;

    private String notice ;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="categorie_id", nullable=false)
    private Categorie categorie;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="client_id", nullable=false)
    private User client;
}
