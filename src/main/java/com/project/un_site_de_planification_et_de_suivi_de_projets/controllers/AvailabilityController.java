package com.project.un_site_de_planification_et_de_suivi_de_projets.controllers;

import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.Availability;
import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.Reservation;
import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.Solution;
import com.project.un_site_de_planification_et_de_suivi_de_projets.exception.ResourceNotFoundException;
import com.project.un_site_de_planification_et_de_suivi_de_projets.repos.AvailabilityRepository;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.ReservationService;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/availabilities")
public class AvailabilityController {
    @Autowired
    private AvailabilityRepository availabilityRepository;
    @Autowired
    private SolutionService solutionService;
    @Autowired
    ReservationService reservationService;



    @GetMapping
    public List<Availability> getAvailabilities() {
        return availabilityRepository.findAll();
    }

    @PostMapping
    public Availability createAvailability(@RequestBody Availability availability) {
        availability.setAvailable(true);
        return availabilityRepository.save(availability);
    }

    @PostMapping("/{id_solution}/Solution")
    public Availability createAvailabilityByIdSolution(@RequestBody Availability availability, @PathVariable Long id_solution) {
        Solution solution = solutionService.findSolutionById(id_solution);
        if(solution != null) {
            availability.setSolution(solution);
            availabilityRepository.save(availability);
            List<Availability> availabilities = solution.getAvailabilities();
            if(availabilities == null) {
                availabilities = new ArrayList<>();
            }
            availabilities.add(availability);
            solution.setAvailabilities(availabilities);
            solutionService.addSolution(solution); // Assuming the solutionService has a save method
            return availability;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Solution not found");
        }
    }


    @PutMapping("/{id}")
    public Availability updateAvailability(@PathVariable Long id, @RequestBody Availability availability) {
        Availability existingAvailability = availabilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Availability", "id", id));
        existingAvailability.setDate(availability.getDate());
        existingAvailability.setAvailable(availability.isAvailable());
        return availabilityRepository.save(existingAvailability);
    }

    @DeleteMapping("/{id}")
    public void deleteAvailability(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
        Availability availability = availabilityRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
        Set<Reservation> reservations = availability.getReservations();
        for (Reservation reservation : reservations){
            reservationService.deleteReservation(reservation.getId());
        }
        availabilityRepository.deleteById(id);
    }

    @GetMapping("/{id_solution}")
    public List<Availability> getAvailabilitiesBySolutionId(@PathVariable Long id_solution) {

        Solution solution = solutionService.findSolutionById(id_solution);
        return solution.getAvailabilities();
    }


}
