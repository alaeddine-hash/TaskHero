package com.project.un_site_de_planification_et_de_suivi_de_projets.controllers;

import com.project.un_site_de_planification_et_de_suivi_de_projets.Security.Jwt.JwtUtils;
import com.project.un_site_de_planification_et_de_suivi_de_projets.config.FileUploadUtil;
import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.*;
import com.project.un_site_de_planification_et_de_suivi_de_projets.payload.request.LoginRequest;
import com.project.un_site_de_planification_et_de_suivi_de_projets.payload.request.SignupRequest;
import com.project.un_site_de_planification_et_de_suivi_de_projets.payload.response.MessageResponse;
import com.project.un_site_de_planification_et_de_suivi_de_projets.payload.response.UserInfoResponse;
import com.project.un_site_de_planification_et_de_suivi_de_projets.repos.UserRepository;
import com.project.un_site_de_planification_et_de_suivi_de_projets.repos.RoleRepository;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.NotificationService;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.ProviderApplicationService;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.UserDetailsImpl;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.project.un_site_de_planification_et_de_suivi_de_projets.entities.ERole.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;

    @Autowired
    ProviderApplicationService providerApplicationService;

    @Autowired
    NotificationService notificationService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println("Incoming username: " + loginRequest.getUsername());
        System.out.println("Incoming password: " + loginRequest.getPassword());

        User userFromDB = userRepository.findByUsername(loginRequest.getUsername()).orElse(null);

        if (userFromDB != null) {
            System.out.println("User details from DB - Username: " + userFromDB.getUsername());
            System.out.println("User details from DB - Password: " + userFromDB.getPassword());
            String encodedPassword = encoder.encode(loginRequest.getPassword());
            System.out.println("User encodedPassword from the out - Password: " + encodedPassword);
            boolean isPasswordMatch = encoder.matches(loginRequest.getPassword(), userFromDB.getPassword());
            System.out.println(" Passwords is matching : " + isPasswordMatch);

        } else {
            System.out.println("No user found in DB with username: " + loginRequest.getUsername());
        }
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        System.out.println("JWT Cookie: " + jwtCookie.toString());


        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(userDetails.getId(),
                        userDetails.getName(),
                        userDetails.getLastname(),
                        userDetails.getBirthday(),
                        userDetails.getPhone(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account ya 3loulou
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getName(),
                signUpRequest.getLastname(),
                signUpRequest.getBirthday(),
                signUpRequest.getPhone(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "provider":
                        Role providerRole = roleRepository.findByName(ROLE_PROVIDER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(providerRole);
                        break;
                    case "assistant":
                        Role assistantRole = roleRepository.findByName(ROLE_ASSISTANT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(assistantRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

    @PostMapping("/provider-application")
    public ResponseEntity<?> applyAsProvider(@RequestParam("fullName") String fullName,
                                             @RequestParam("email") String email,
                                             @RequestParam("phone") String phone,
                                             @RequestParam("address") String address,
                                             @RequestParam("password") String password,
                                             @RequestParam("certificationFile") @Valid @NotNull MultipartFile certificationFile) {
        try {

            System.out.println(fullName);
            System.out.println(email);
            System.out.println(phone);
            System.out.println(address);
            System.out.println(password);
            System.out.println(certificationFile);

            // Validate form data
            if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()|| password.isEmpty()) {
                return new ResponseEntity<>("All fields are required.", HttpStatus.BAD_REQUEST);
            }

            if (certificationFile.isEmpty()) {
                return new ResponseEntity<>("Certification file is required.", HttpStatus.BAD_REQUEST);
            }

            // Check if user already exists
            User existingUser = userService.findUserByEmail(email);
            if (existingUser != null) {
                return new ResponseEntity<>("Error: Email is already in use!", HttpStatus.BAD_REQUEST);
            }

            // Save file to your server
            String certificationFilePath = FileUploadUtil.saveFile("certifications", certificationFile);

            // Create new user

            User user = new User(fullName,
                    fullName,
                    fullName,
                    LocalDate.now(),
                    phone,
                    email,
                    encoder.encode(password));

            // Assign provider role to user
            Role providerRole = roleRepository.findByName(ROLE_USER).orElseThrow();
            Set<Role> roles = new HashSet<>();
            roles.add(providerRole);
            user.setRoles(roles);

            // Create and save ProviderApplication
            ProviderApplication application = new ProviderApplication();
            application.setName(fullName);
            application.setEmail(email);
            application.setPhone(phone);
            application.setAddress(address);
            application.setApplicant(user);
            application.setCertification(certificationFile.getBytes());  // assuming you have set a @Lob field in ProviderApplication for this
            application.setStatus(ApplicationStatus.PENDING);

            // Save user to DB
            userRepository.save(user);

            // Save provider application to DB
            providerApplicationService.apply(application);

            // Create notification
            String message = "Your provider application has been submitted , wait while our team will respond you soon you are welcome .";
            notificationService.createNotification(message, user);

            return new ResponseEntity<>("Provider application submitted successfully.", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace(); // Print the exception stack trace for debugging purposes
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

