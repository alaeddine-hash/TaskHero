package com.project.un_site_de_planification_et_de_suivi_de_projets.controllers;

import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.ApplicationStatus;
import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.ProviderApplication;
import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.ReviewApplicationRequest;
import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.User;
import com.project.un_site_de_planification_et_de_suivi_de_projets.repos.ProviderApplicationRepository;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.NotificationService;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.ProviderApplicationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.project.un_site_de_planification_et_de_suivi_de_projets.entities.ApplicationStatus.APPROVED;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/applications")
public class ProviderApplicationController {

    private final ProviderApplicationService providerApplicationService;

    private final ProviderApplicationRepository repository;
    private final NotificationService notificationService;

    // Inject repository through constructor
    public ProviderApplicationController(ProviderApplicationRepository repository, ProviderApplicationService providerApplicationService,NotificationService notificationService) {
        this.repository = repository;
        this.providerApplicationService = providerApplicationService;
        this.notificationService = notificationService;
    }
    @PostMapping("/apply")
    public ProviderApplication apply(@RequestBody ProviderApplication application) {
        return providerApplicationService.apply(application);
    }

    @GetMapping
    public List<ProviderApplication> getAllApplications() {
        return providerApplicationService.getAllApplications();
    }

    @PostMapping
    public ProviderApplication createApplication(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address,
            @RequestParam("certification") MultipartFile certificationFile,
            @RequestParam("applicant_id") Long applicantId
    ) throws IOException {
        ProviderApplication application = new ProviderApplication();

        // Set fields on application
        application.setName(name);
        application.setEmail(email);
        application.setPhone(phone);
        application.setAddress(address);

        // Convert MultipartFile to byte[]
        application.setCertification(certificationFile.getBytes());

        // Set the applicant. This is a simplified version. In a real application,
        // you would fetch the actual User entity from the database.
        User applicant = new User();
        applicant.setId(applicantId);
        application.setApplicant(applicant);

        // Set initial status
        application.setStatus(ApplicationStatus.PENDING);

        // Save and return application
        return repository.save(application);
    }

    @PutMapping("/{id}/review")
    public ProviderApplication reviewApplication(@PathVariable Long id, @RequestBody ReviewApplicationRequest request) {
        ApplicationStatus status = request.getStatus();
        ProviderApplication reviewedApplication = providerApplicationService.reviewApplication(id, status);

        // Create notification

        if (status == APPROVED){

        String message = " congratulation Your provider application has been " + status.name().toLowerCase() + ".";
            notificationService.createNotification(message, reviewedApplication.getApplicant());
                                }
        else {
            String message = " sorry Your provider application has been " + status.name().toLowerCase() + ". "+ "we are waiting your new application where you can improve your certification";
            notificationService.createNotification(message, reviewedApplication.getApplicant());

             }


        return reviewedApplication;
    }

}
