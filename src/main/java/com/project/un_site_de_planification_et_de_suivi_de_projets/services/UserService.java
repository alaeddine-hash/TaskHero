package com.project.un_site_de_planification_et_de_suivi_de_projets.services;

import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.User;
import com.project.un_site_de_planification_et_de_suivi_de_projets.repos.UserRepository;
import com.project.un_site_de_planification_et_de_suivi_de_projets.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;


@Service
@Transactional
public class UserService implements UserDetailsService {
    private static UserRepository UserRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository UserRepo, PasswordEncoder passwordEncoder) {
        this.UserRepo = UserRepo;

        this.passwordEncoder = passwordEncoder;
    }

    public User addUser(User employee) {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        return UserRepo.save(employee);
    }

    public List<User> findAllUsers() {
        return UserRepo.findAll();
    }

    public User updateUser(User employee) {
        return UserRepo.save(employee);
    }

    public User findUserById(Long id) {
        return UserRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id " + id + " was not found"));
    }

    public User findUserByEmail(String email) {
        return UserRepo.findByEmail(email).orElse(null);
    }

    public void deleteUser(Long id) {
        UserRepo.deleteById(id);
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = UserRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }

}
