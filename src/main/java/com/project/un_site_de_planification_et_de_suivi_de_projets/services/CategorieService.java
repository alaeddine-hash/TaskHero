package com.project.un_site_de_planification_et_de_suivi_de_projets.services;

import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.Categorie;
import com.project.un_site_de_planification_et_de_suivi_de_projets.exception.UserNotFoundException;
import com.project.un_site_de_planification_et_de_suivi_de_projets.repos.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorieService {
    private static CategorieRepository categorieRepository;


    @Autowired
    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;


    }

    public Categorie addCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    public List<Categorie> findAllCategories() {
        return categorieRepository.findAll();
    }

    public Categorie updateCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    public Categorie findCategorieById(Long id) {
        return categorieRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("categorie by id " + id + " was not found"));
    }

    public Categorie findCategorieByName(String name) {
        return categorieRepository.findCategorieByName(name)
                .orElseThrow(() -> new UserNotFoundException("categorie by name " + name + " was not found"));
    }

    public void deleteCategorie(Long id) {
        categorieRepository.deleteById(id);
    }

  /*  public List<Categorie> getCategoriesByIdSolution(Long idSolution) {
        Solution solution = solutionService.findSolutionById(idSolution);
        return solution.getCategories();
    }
   */
}
