package com.project.un_site_de_planification_et_de_suivi_de_projets.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity

public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_availability;
    private LocalDate date;

    private boolean isAvailable;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }



    public Long getId_availability() {
        return id_availability;
    }

    public void setId_availability(Long id_availability) {
        this.id_availability = id_availability;
    }
    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }


    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="solution_id")
    private Solution solution;

    @JsonIgnore
    @OneToMany(mappedBy="availability")
    private Set<Reservation> reservations;

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }
}
