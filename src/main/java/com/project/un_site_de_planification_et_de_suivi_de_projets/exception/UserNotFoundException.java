package com.project.un_site_de_planification_et_de_suivi_de_projets.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
