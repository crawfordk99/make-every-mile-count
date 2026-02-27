package controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ViewController handles serving Thymeleaf templates for the web UI.
 */
@Controller
public class ViewController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home");
        return "index";
    }

    @GetMapping("/calculate")
    public String calculate(Model model) {
        model.addAttribute("title", "Calculate");
        return "calculate";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Login");
        return "login";
    }

    @GetMapping("/saved")
    public String saved(Model model) {
        model.addAttribute("title", "Saved Calculations");
        return "saved";
    }
}
