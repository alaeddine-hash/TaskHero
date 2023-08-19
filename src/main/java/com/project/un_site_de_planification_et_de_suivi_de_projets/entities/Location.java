package com.project.un_site_de_planification_et_de_suivi_de_projets.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.geo.Point;

import javax.persistence.*;

@Entity
public class Location {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "latitude")
    private Double lat;

    @Column(name = "longitude")
    private Double lng;

    @Column
    private String name;

    @JsonIgnore
    @OneToOne
    Solution solution;

    @JsonIgnore
    @OneToOne
    User user;

    public Location(double lat, double lng, String address) {
        this.lat = lat;
        this.lng = lng;
        this.name = address;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location() {}

    public Location(Double latitude, Double longitude) {
        this.lat = latitude;
        this.lng = longitude;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double latitude) {
        this.lat = latitude;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double longitude) {
        this.lng = longitude;
    }


    public static Location fromPoint(Point point) {
        Location location = new Location();
        location.setLat(point.getY());
        location.setLng(point.getX());
        return location;
    }


}
