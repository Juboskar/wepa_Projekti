package projekti;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/welcome")
    public String helloWorld(@ModelAttribute("accountdto") AccountDto accountdto) {
        return "signup";
    }

    @GetMapping("/redirectlogin")
    public String signup() {
        return "redirect:/welcome";
    }

    @PostMapping("/welcome")
    public String add(@Valid @ModelAttribute("accountdto") AccountDto accountDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        if (accountService.save(accountDto.getUsername(),
                passwordEncoder.encode(accountDto.getPassword()))) {
            return "registersucces";
        }
        return "redirect:/welcome";
    }
}
