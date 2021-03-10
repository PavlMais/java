package app.web;

import app.ResetEmail;
import app.entities.User;
import app.mail.EmailService;
import app.repositories.UserRepository;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@Controller
public class AccountController {
    private final EmailService emailService;
    private final UserService userService;
    private final UserRepository userRepository;



    @Autowired
    public AccountController(EmailService emailService, UserService userService, UserRepository userRepository)
    {
        this.userService = userService;
        this.emailService= emailService;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String login(Model model) {

        return "account/login";
    }

    @PostMapping("/sendPasswordReset")
    public String sendPasswordReset(final HttpServletRequest request, @Valid ResetEmail resetEmail, Model model){
        String email = resetEmail.getEmail();

        User user = userRepository.findByEmail(email);

        String token = UUID.randomUUID().toString();

        String recoverUrl = "/user/changePassword?token=" + token;

        userService.createPasswordResetTokenForUser(user, token);

        emailService.sendSimpleMessage(email,"Успішна реєстрації","Спасібо Token: " + token);

        System.out.println("Emial sent to:" + resetEmail.getEmail());
        return "account/waitEmail";
    }


    @GetMapping("/forgotpassword")
    public String forgotPass(){
        return "account/forgotPassword";
    }
}
