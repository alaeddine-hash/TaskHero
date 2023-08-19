package com.project.un_site_de_planification_et_de_suivi_de_projets.services;

import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.Message;
import com.project.un_site_de_planification_et_de_suivi_de_projets.exception.UserNotFoundException;
import com.project.un_site_de_planification_et_de_suivi_de_projets.repos.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class MessageService {

    private static MessageRepository messageRepository;


    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MessageService(MessageRepository messageRepository, PasswordEncoder passwordEncoder) {
        this.messageRepository = messageRepository;

        this.passwordEncoder = passwordEncoder;

    }

    public MessageService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Message addMessage(Message message) {

        return messageRepository.save(message);
    }

    public List<Message> findAllMessages() {
        return messageRepository.findAll();
    }

    public Message updateMessage(Message message) {
        return messageRepository.save(message);
    }

    public Message findMesageById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id " + id + " was not found"));
    }

    public void deleteMessage(Long id){
        messageRepository.deleteById(id);
    }

    public List<Message> getMessagesBySenderId(Long id){
        return messageRepository.getMessagesBySender_Id(id);
    }

    public List<Message> getMessagesByRecipientId(Long id){
        return messageRepository.getMessagesByRecipient_id(id);
    }


}
