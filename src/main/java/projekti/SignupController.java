package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignupController {

    @Autowired
    AccountRepository accountRepository;

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
        if (accountRepository.findByUsername(username) != null) {
            return "redirect:/welcome";
        }
        Account a = new Account(username, passwordEncoder.encode(password));
        accountRepository.save(a);
        return "redirect:/welcome";
    }

}
