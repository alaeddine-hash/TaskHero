package com.project.un_site_de_planification_et_de_suivi_de_projets.controllers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.Availability;
import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.Notification;
import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.Reservation;
import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.User;
import com.project.un_site_de_planification_et_de_suivi_de_projets.repos.AvailabilityRepository;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.NotificationService;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.ReservationService;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    ReservationService reservationService ;
   AvailabilityRepository availabilityRepository;
    UserService userService;

    NotificationService notificationService;

    @Autowired
    public ReservationController(ReservationService reservationService, AvailabilityRepository availabilityRepository, UserService userService, NotificationService notificationService) {
        this.reservationService = reservationService;
        this.availabilityRepository = availabilityRepository;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @PostMapping("/add/{id_user}/userId/{id_availability}/availabilityId")
    @ResponseBody
    public Reservation addNewReservation(@RequestBody Reservation reservation, @PathVariable Long id_user, @PathVariable Long id_availability) throws Exception {
        Reservation reservationObj = new Reservation();
        reservationObj = reservation;
        User user = userService.findUserById(id_user);
        Availability availability = availabilityRepository.findById(id_availability).orElseThrow(ChangeSetPersister.NotFoundException::new);
        availability.setAvailable(false);
        availabilityRepository.save(availability);

        reservationObj.setAvailability(availability);
        reservationObj.setClient(user);

        // Create in-app notification for provider
        String message = "A new reservation has been made for your solution: " + availability.getSolution().getTitleSolution() + " for  : " + availability.getDate() + "by " + user.getName() + " with email : " +
                user.getEmail() + " , PhoneNumber : " + user.getPhone();
        String messageForTheUser = "your reservation have been submitted successfully , you will be contacted by the provider :   " + availability.getSolution().getProvider().getName() + " with the email : " + availability.getSolution().getProvider().getEmail() + " , PhoneNumber : " + availability.getSolution().getProvider().getPhone();
        notificationService.createNotification(message, availability.getSolution().getProvider());
        notificationService.createNotification(messageForTheUser, user);
        return reservationService.addReservation(reservation);
    }

    @JsonIgnore
    @GetMapping("/all")
    @ResponseBody
    public List<Reservation> allReservations(){
        return reservationService.findAllReservations();
    }


    @GetMapping("/id/{id}")
    @ResponseBody
    public Reservation findReservationById(@PathVariable("id") long id){
        return reservationService.findReservationById(id);
    }


    @PutMapping("/update")
    @ResponseBody
    public void updateReservation(@RequestBody Reservation reservation){
        reservationService.updateReservation(reservation); }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public void deleteReservation(@PathVariable("id") long id){
        reservationService.deleteReservation(id); }

    @GetMapping("/providers/{providerId}/notifications")
    public List<Notification> getNotificationsForProvider(@PathVariable Long providerId) {
        return notificationService.getNotificationsForProvider(providerId);
    }


}
