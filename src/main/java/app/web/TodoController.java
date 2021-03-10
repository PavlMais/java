package app.web;

import app.entities.User;
import app.mail.EmailService;
import app.repositories.TodoRepository;
import app.repositories.UserRepository;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TodoController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TodoRepository todoRepository;

    @Autowired
    public TodoController(UserRepository userRepository,
                          UserService userService,
                          PasswordEncoder passwordEncoder,
                          EmailService emailService,
                          TodoRepository todoRepository)
    {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder=passwordEncoder;
        this.emailService= emailService;
        this.todoRepository = todoRepository;
    }





    @GetMapping("/todos/{id}")
    public String home(@PathVariable("id") Long id, Model model) {

        model.addAttribute("todos", todoRepository.findById(id));

        return "todo";
    }
}
