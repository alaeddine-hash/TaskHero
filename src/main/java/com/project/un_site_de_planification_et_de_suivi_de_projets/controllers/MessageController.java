package com.project.un_site_de_planification_et_de_suivi_de_projets.controllers;

import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.Message;
import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.User;
import com.project.un_site_de_planification_et_de_suivi_de_projets.repos.UserRepository;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.MessageService;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    final
    MessageService messageService ;

    @Autowired
    UserRepository userService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/add")
    @ResponseBody
    public Message add_New_Message(@RequestBody Message message) {
        return messageService.addMessage(message);
    }


    @GetMapping("/all")
    @ResponseBody
    public List<Message> all_messages(){
        return messageService.findAllMessages();
    }


    @GetMapping("/id/{id}")
    @ResponseBody
    public Message message_id(@PathVariable("id") long id){
        return messageService.findMesageById(id);
    }

    @GetMapping("/username/{username}")
    @ResponseBody
    public User getUserByUsername(@PathVariable String username){
        return userService.findByUsername(username).orElse(null);
    }

    @GetMapping("/username_sender/{username}")
    @ResponseBody
    public List<Message> getMessagesByUsernameSender(@PathVariable String username){
        User user =  userService.findByUsername(username).orElse(null);
        return messageService.getMessagesBySenderId(user.getId());
    }

    @GetMapping("/username_recipient/{username}")
    @ResponseBody
    public List<Message> getMessagesByUsernameProvider(@PathVariable String username){
        User user =  userService.findByUsername(username).orElse(null);
        return messageService.getMessagesBySenderId(user.getId());
    }


    @PutMapping("/update")
    @ResponseBody
    public void Mettre_A_jours_message(@RequestBody Message message){messageService.updateMessage(message); }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public void supp_message(@PathVariable("id") long id){messageService.deleteMessage(id); }



}
