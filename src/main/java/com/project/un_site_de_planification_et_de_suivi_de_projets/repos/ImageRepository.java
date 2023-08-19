package com.project.un_site_de_planification_et_de_suivi_de_projets.repos;

import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.Image;
import jdk.jfr.Registered;
import org.springframework.data.repository.CrudRepository;
@Registered
public interface ImageRepository extends CrudRepository<Image, String> {
}
