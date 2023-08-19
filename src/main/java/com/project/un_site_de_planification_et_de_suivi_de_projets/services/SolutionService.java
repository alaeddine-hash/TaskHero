package com.project.un_site_de_planification_et_de_suivi_de_projets.services;

import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.Availability;
import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.Categorie;
import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.Solution;
import com.project.un_site_de_planification_et_de_suivi_de_projets.exception.UserNotFoundException;
import com.project.un_site_de_planification_et_de_suivi_de_projets.repos.AvailabilityRepository;
import com.project.un_site_de_planification_et_de_suivi_de_projets.repos.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.availability.AvailabilityState;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.lang.System.in;

@Service
public class SolutionService {

    private static SolutionRepository solutionRepository;
    private CategorieService categorieService;

    private AvailabilityRepository availabilityRepository;

    @Autowired
    public SolutionService(SolutionRepository solutionRepository, CategorieService categorieService, AvailabilityRepository availabilityRepository) {
        this.solutionRepository = solutionRepository;
        this.categorieService = categorieService ;
        this.availabilityRepository = availabilityRepository;

    }

    public Solution addSolution(Solution solution) {
        solution.setProviderUsername(solution.getProvider().getUsername());
        return solutionRepository.save(solution);
    }

    public List<Solution> findAllSolutions() {
        return solutionRepository.findAll();
    }

    public Solution updateSolution(Solution solution) {
        return solutionRepository.save(solution);
    }

    public Solution findSolutionById(Long id) {
        return solutionRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("solution by id " + id + " was not found"));
    }

    public void deleteSolution(Long id) {
        solutionRepository.deleteById(id);
    }

    public List<Solution> getSolutionsByCategorieId(Long categorieId){
        Categorie categorie = categorieService.findCategorieById(categorieId);
        return categorie.getSolutions().stream().toList();
    }

    public Solution getSolutionByIdAvalabilty(Long availabilityId){
        Optional<Availability> availability = availabilityRepository.findById(availabilityId);

        return availability.get().getSolution();
    }

}
