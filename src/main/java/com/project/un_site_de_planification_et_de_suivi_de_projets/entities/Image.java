package com.project.un_site_de_planification_et_de_suivi_de_projets.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String fileName;
    private String fileType;
    @Lob
    private byte[] data;

    @JsonIgnore
    @OneToOne(mappedBy = "image")
    User user;

    @JsonIgnore
    @OneToOne(mappedBy = "image")
    Categorie categorie;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="id_solution")
    private Solution solution;

    public Image(String id, String fileName, String fileType, byte[] data) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }

    public Image(Object o, String fileName, String fileType, byte[] data) {
        this.id = o.toString();
        this.fileName = fileName;
        this.fileType = fileType ;
        this.data = data;
    }
}
