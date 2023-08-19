package com.project.un_site_de_planification_et_de_suivi_de_projets.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "messages")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
//@JsonIdentityInfo(property = "msg_id", generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private  long msg_id;

    private String object ;
    private LocalDate date;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="sender_id", nullable=false)
    private User sender;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="recipient_id", nullable=false)
    private User recipient;
}
