package com.project.un_site_de_planification_et_de_suivi_de_projets.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_solution;

    @NotBlank
    @Column(length = 20)
    private String titleSolution;



    @NotBlank
    @Column(length = 255)
    private String description;

    @Column(length = 40)
    private String providerUsername = "???";

    public Solution(Long id_solution, String titleSolution, String description, String providerUsername, double price, List<Availability> availabilities, List<Image> images, Location location, Categorie categorie, User provider) {
        this.id_solution = id_solution;
        this.titleSolution = titleSolution;
        this.description = description;
        this.providerUsername = providerUsername;
        this.price = price;
        this.availabilities = availabilities;
        this.images = images;
        this.location = location;
        this.categorie = categorie;
        this.provider = provider;
    }

    public String getProviderUsername() {
        return providerUsername;
    }

    public void setProviderUsername(String providerUsername) {
        this.providerUsername = providerUsername;
    }

    @Column(length = 40)
    private double price;

    @OneToMany(mappedBy = "solution")
    private List<Availability> availabilities ;

    @OneToMany(mappedBy = "solution")
    private List<Image> images ;

    @OneToOne(mappedBy = "solution")
    Location location;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="categorie_id", nullable=false)
    private Categorie categorie;

   @JsonIgnore
    @ManyToOne
    @JoinColumn(name="provider_id", nullable=false)
    private User provider;

    public Long getId_solution() {
        return id_solution;
    }

    public void setId_solution(Long id_solution) {
        this.id_solution = id_solution;
    }

    public List<Availability> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(List<Availability> availabilities) {
        this.availabilities = availabilities;
    }


   // @Type(type = "org.hibernate.spatial.GeometryType")
   // private Point location;



    public String getTitleSolution() {
        return titleSolution;
    }

    public void setTitleSolution(String titleSolution) {
        this.titleSolution = titleSolution;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }



    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }




    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public User getProvider() {
        return provider;
    }

    public void setProvider(User provider) {
        this.provider = provider;
    }


}
