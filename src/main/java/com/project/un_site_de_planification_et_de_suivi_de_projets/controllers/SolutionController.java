package com.project.un_site_de_planification_et_de_suivi_de_projets.controllers;

import com.project.un_site_de_planification_et_de_suivi_de_projets.config.FileUploadUtil;
import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.*;
import com.project.un_site_de_planification_et_de_suivi_de_projets.repos.AvailabilityRepository;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.CategorieService;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.ImageService;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.SolutionService;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/solutions")
public class SolutionController {

    @Autowired
    private SolutionService solutionService;

    @Autowired
    UserService userService;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private CategorieService categorieService;

    @Autowired
    ImageService imageService;



    @GetMapping
    public List<Solution> getSolutions() {
        return solutionService.findAllSolutions();
    }


    @PostMapping
    public Solution createSolution(@RequestBody Solution solution) {

        System.out.println("Received solution: " + solution);
        return solutionService.addSolution(solution);
    }

    @PostMapping("/{id_solution}/id_solution")
    public Solution createAvailebilitieForSolution(@PathVariable Long id_solution, @RequestBody Availability availability) {
        Solution solution = solutionService.findSolutionById(id_solution);
        Availability availabilityObject = new Availability();
        availabilityObject = availability;
        availabilityObject.setSolution(solution);
        availabilityRepository.save(availabilityObject);
        List<Availability> availabilities = new ArrayList<Availability>();
        availabilities = solution.getAvailabilities();
        availabilities.add(availabilityObject);
        solution.setAvailabilities(availabilities);
        return solutionService.updateSolution(solution);
    }

    @PostMapping("addSolutionBy/{categorie_id}/provider/{provider_id}")
    public Solution addSolutionByCategorieId(@PathVariable Long categorie_id, @RequestBody Solution solution, @PathVariable Long provider_id){
        Categorie categorie = categorieService.findCategorieById(categorie_id);
        User user = userService.findUserById(provider_id);
        Set<Solution> solutionListUser = user.getSolutions();
        solutionListUser.add(solution);
        user.setSolutions(solutionListUser);
        Set<Solution> solutionList = categorie.getSolutions();
        solutionList.add(solution);
        categorie.setSolutions(solutionList);

        solution.setCategorie(categorie);
        solution.setProvider(user);
        return solutionService.addSolution(solution);
    }

    @PostMapping("addSolution")
    public Solution addSolution(@RequestBody Solution solution) {
        Long categorie_id = solution.getCategorie().getId();
        Categorie categorie = categorieService.findCategorieById(categorie_id);

        Set<Solution> solutionList = categorie.getSolutions();
        solutionList.add(solution);
        categorie.setSolutions(solutionList);

        solution.setCategorie(categorie);
        return solutionService.addSolution(solution);
    }

    @GetMapping("/{id}/services")
    @ResponseBody
    public List<Solution> findSolutionsByCategorieId(@PathVariable("id") Long id){
        return solutionService.getSolutionsByCategorieId(id);
    }

    @GetMapping("/{id}/servicesByProvider")
    @ResponseBody
    public Set<Solution> findSolutionsByProviderId(@PathVariable("id") Long id){
        return userService.findUserById(id).getSolutions();
    }

    @PostMapping("/{sid}/add-image")
    public void addImage(@PathVariable Long sid, @RequestParam("image") MultipartFile multipartFile) throws IOException {

        Solution solution = solutionService.findSolutionById(sid);
        if (!multipartFile.isEmpty()) {
            String orgFileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String ext = orgFileName.substring(orgFileName.lastIndexOf("."));
            String uploadDir = "src/main/resources/static/categories-photos/";
            String fileName = "solution-" + solution.getId_solution() + ext;
            Image img = new Image(multipartFile, fileName, ext, multipartFile.getBytes());
            Image image = imageService.addImageLa(img);
            List<Image> images = solution.getImages();
            images.add(image);
            solution.setImages(images);
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
            solutionService.updateSolution(solution);
        }
    }
}
