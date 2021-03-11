package app.web;


import app.entities.Task;
import app.entities.Todo;
import app.entities.User;
import app.mail.EmailService;
import app.repositories.TaskRepository;
import app.repositories.TodoRepository;
import app.repositories.UserRepository;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class HomeController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TaskRepository taskRepository;

    @Autowired
    public HomeController(UserRepository userRepository,
                          UserService userService,
                          PasswordEncoder passwordEncoder,
                          EmailService emailService,
                          TaskRepository taskRepository)
    {
        this.userRepository=userRepository;
        this.userService = userService;
        this.passwordEncoder=passwordEncoder;
        this.emailService= emailService;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/tasks")
    public String tasks(Model model){

        model.addAttribute("tasks", taskRepository.findAll());

        return "index";
    }

    @GetMapping("/")
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/contact")
    public String contactPage() {
        return "contact";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    @GetMapping("/createTask")
    public String createTask(Task task) {
        return "createTask";
    }
    @GetMapping("/register")
    public String register(User user) {
        return "create";
    }

    @PostMapping("/register")
    public String addUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "create";
        }

        if(userRepository.findByEmail(user.getEmail()) != null){
            model.addAttribute("error", "Email is used");
            return "create";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        emailService.sendSimpleMessage(user.getEmail(),"Успішна реєстрації","Спасібо");
        return "redirect:/tasks";
    }

    @PostMapping("/createTask")
    public String createTask(@Valid Task task, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "createTask";
        }

        taskRepository.save(task);

        return "redirect:/tssks";
    }

    @GetMapping("/edit/{id}")
    public String editTask(@PathVariable("id") Long id, Model model) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));
        model.addAttribute("task", task);
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable("id") Long id, Task task,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            task.setId(id);
            return "edit";
        }
        taskRepository.save(task);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));
        taskRepository.delete(task);
        return "redirect:/";
    }
}
