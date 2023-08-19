package com.project.un_site_de_planification_et_de_suivi_de_projets.entities;

import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.ApplicationStatus;
import javax.persistence.*;

@Entity
@Table(name = "provider_applications")
public class ProviderApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    public ProviderApplication(Long id, String name, String email, String password, String phone, String address, ApplicationStatus status, byte[] certification, User applicant) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.status = status;
        this.certification = certification;
        this.applicant = applicant;
    }

    @Column
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status; // PENDING, APPROVED, REJECTED

    public ProviderApplication() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public byte[] getCertification() {
        return certification;
    }

    public void setCertification(byte[] certification) {
        this.certification = certification;
    }

    public User getApplicant() {
        return applicant;
    }

    public void setApplicant(User applicant) {
        this.applicant = applicant;
    }

    // Assuming you are storing the certification document as a byte array
    @Lob
    private byte[] certification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id")
    private User applicant; // User who is applying

    public ProviderApplication(Long id, String name, String email, String phone, String address, ApplicationStatus status, byte[] certification, User applicant) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.status = status;
        this.certification = certification;
        this.applicant = applicant;
    }
}
