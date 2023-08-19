package com.project.un_site_de_planification_et_de_suivi_de_projets.services;

import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.ApplicationStatus;
import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.ProviderApplication;
import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.Role;
import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.User;
import com.project.un_site_de_planification_et_de_suivi_de_projets.exception.ResourceNotFoundException;
import com.project.un_site_de_planification_et_de_suivi_de_projets.repos.ProviderApplicationRepository;
import com.project.un_site_de_planification_et_de_suivi_de_projets.repos.RoleRepository;
import com.project.un_site_de_planification_et_de_suivi_de_projets.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.project.un_site_de_planification_et_de_suivi_de_projets.entities.ApplicationStatus.APPROVED;
import static com.project.un_site_de_planification_et_de_suivi_de_projets.entities.ERole.ROLE_PROVIDER;
import static com.project.un_site_de_planification_et_de_suivi_de_projets.entities.ERole.ROLE_USER;

@Service
public class ProviderApplicationService {
    @Autowired
    UserService userService ;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private ProviderApplicationRepository providerApplicationRepository;

    @Autowired
    RoleRepository roleRepository;

    public ProviderApplication apply(ProviderApplication application) {
        // Here you can implement the logic for applying to become a provider

        application.setStatus(ApplicationStatus.PENDING);
        return providerApplicationRepository.save(application);
    }

    public List<ProviderApplication> getAllApplications() {
        // This will be used by the admin to see all applications
        return providerApplicationRepository.findAll();
    }

    public ProviderApplication reviewApplication(Long id, ApplicationStatus status) {
        // This will be used by the admin to approve or reject applications
        ProviderApplication application = providerApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        if (status == APPROVED){

            User user = userService.findUserById(application.getApplicant().getId());
            // Assign provider role to user
            Role providerRole = roleRepository.findByName(ROLE_PROVIDER).orElseThrow();
            Set<Role> roles = new HashSet<>();
            roles.add(providerRole);
            user.setRoles(roles);

            // update user to DB
            userRepository.save(user);


        }
        application.setStatus(status);
        return providerApplicationRepository.save(application);
    }
}

