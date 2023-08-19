package com.project.un_site_de_planification_et_de_suivi_de_projets.controllers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.un_site_de_planification_et_de_suivi_de_projets.config.FileUploadUtil;
import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.Categorie;
import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.Image;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.CategorieService;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategorieController {
    CategorieService categorieService;
    ImageService imageService;



    @Autowired
    public CategorieController(CategorieService categorieService, ImageService imageService) {
        this.categorieService = categorieService;
        this.imageService = imageService;

    }

    @PostMapping("/add")
    @ResponseBody
    public Categorie addNewCategorie(@RequestBody Categorie categorie) throws Exception {
        return categorieService.addCategorie(categorie);
    }

    @JsonIgnore
    @GetMapping("/all")
    @ResponseBody
    public List<Categorie> allCategories(){
        return categorieService.findAllCategories();
    }


    @GetMapping("/id/{id}")
    @ResponseBody
    public Categorie findCategorieById(@PathVariable("id") long id){
        return categorieService.findCategorieById(id);
    }


    @PutMapping("/update")
    @ResponseBody
    public void updateCategorie(@RequestBody Categorie categorie){
        categorieService.updateCategorie(categorie); }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public void deleteCategorie(@PathVariable("id") long id){
        //delete all products attached with this categorie
        categorieService.deleteCategorie(id); }


  /*  @GetMapping("/id_space/{id_solution}")
    @ResponseBody
    public List<Categorie> getCategoriesByIdSolution( @PathVariable String id_solution) {
        return categorieService.getCategoriesByIdSolution( id_solution);
    }
   */

    @PostMapping("/{sid}/add-image")
    public void addImage(@PathVariable Long sid, @RequestParam("image") MultipartFile multipartFile) throws IOException {

        Categorie categorie = categorieService.findCategorieById(sid);
        if (!multipartFile.isEmpty()) {
            String orgFileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String ext = orgFileName.substring(orgFileName.lastIndexOf("."));
            String uploadDir = "src/main/resources/static/categories-photos/";
            String fileName = "categorie-" + categorie.getId() + ext;
            Image img = new Image(multipartFile, fileName, ext, multipartFile.getBytes());
            Image image = imageService.addImageLa(img);
            categorie.setImage(image);
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
            categorieService.updateCategorie(categorie);
        }
    }



}
