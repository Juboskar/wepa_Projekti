package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignupController {

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/welcome")
    public String helloWorld(Model model) {
        return "signup";
    }

    @GetMapping("/redirectlogin")
    public String signup() {
        return "redirect:/welcome";
    }

    @PostMapping("/welcome")
    public String add(@RequestParam String username, @RequestParam String password) {
        accountService.save(username, passwordEncoder.encode(password));
        return "redirect:/welcome";
    }

}
