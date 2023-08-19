package com.project.un_site_de_planification_et_de_suivi_de_projets.controllers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.un_site_de_planification_et_de_suivi_de_projets.config.FileUploadUtil;
import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.*;
import com.project.un_site_de_planification_et_de_suivi_de_projets.repos.RoleRepository;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.ImageService;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.NotificationService;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.ProviderApplicationService;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.UserService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.project.un_site_de_planification_et_de_suivi_de_projets.entities.ERole.ROLE_USER;


@RestController
@RequestMapping("/api/users")
public class UserController {


   private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    private ImageService imageService;

   private final ProviderApplicationService providerApplicationService;

     private  final  NotificationService notificationService;
    @Autowired
    public UserController(UserService userService, NotificationService notificationService, ImageService imageService, RoleRepository roleRepository, ProviderApplicationService providerApplicationService) {

        this.userService = userService;
        this.imageService = imageService ;
        this.roleRepository = roleRepository;
        this.providerApplicationService = providerApplicationService ;
        this.notificationService = notificationService;

    }

    @PostMapping("/add")
    @ResponseBody
    public User add_New_User(@RequestBody User user) {
        return userService.addUser(user);
    }

    @JsonIgnore
    @GetMapping("/all")
    @ResponseBody
    public List<User> all_users(){
        return userService.findAllUsers();
    }


    @GetMapping("/id/{id}")
    @ResponseBody
    public User user_id(@PathVariable("id") long id){
        return userService.findUserById(id);
    }


    @PutMapping("/update")
    @ResponseBody
    public void updateUser(@RequestBody User user){
        userService.updateUser(user); }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public void supp_user(@PathVariable("id") long id){
        userService.deleteUser(id); }

    @PostMapping("/{sid}/add-image")
    public void addImage(@PathVariable Long sid, @RequestParam("image") MultipartFile multipartFile) throws IOException {

        User user = userService.findUserById(sid);

        userService.addUser(user);
        if (!multipartFile.isEmpty()) {
            String orgFileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String ext = orgFileName.substring(orgFileName.lastIndexOf("."));
            String uploadDir = "users-photos/";
            String fileName = "user-" + user.getId() + ext;
            Image img = new Image(multipartFile, fileName, ext, multipartFile.getBytes());
            Image image = imageService.addImageLa(img);
            user.setImage(image);
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
            userService.updateUser(user);
        }
    }



    @RequestMapping(value = "/{sid}/display-image")
    public void getUserPhoto(HttpServletResponse response, @PathVariable("sid") long sid) throws Exception {
        User user = userService.findUserById(sid);
        Image image = user.getImage();

        if(image != null) {
            response.setContentType(image.getFileType());
            InputStream inputStream = new ByteArrayInputStream(image.getData());
            IOUtils.copy(inputStream, response.getOutputStream());
        }
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
            userService.addUser(user);

            // Save provider application to DB
            providerApplicationService.apply(application);

            // Create notification
            String message = "Your provider application has been submitted.";
            notificationService.createNotification(message, user);

            return new ResponseEntity<>("Provider application submitted successfully.", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace(); // Print the exception stack trace for debugging purposes
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
