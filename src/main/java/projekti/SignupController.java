package projekti;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/welcome")
    public String helloWorld(@ModelAttribute("accountDto") AccountDto accountDto) {
        return "index";
    }

    @GetMapping("/redirectlogin")
    public String signup() {
        return "redirect:/welcome";
    }

    @PostMapping("/welcome")
    public String add(Model model,
            @Valid @ModelAttribute("accountDto") AccountDto accountDto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "index";
        }
        if (accountService.save(
                accountDto.getUsername(),
                passwordEncoder.encode(accountDto.getPassword()),
                accountDto.getName(),
                accountDto.getUserpath())) {
            return "registersucces";
        }
        String message = "Käyttäjätunnus tai polku ei ole saatavilla.";
        model.addAttribute("message", message);
        return "index";
    }

    @GetMapping("/deleteaccount")
    public String deleteAccount() {
        return "deleteaccount";
    }

    @GetMapping("/deleteaccountconfirmed")
    public String deleteAccountConfirmed() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        accountService.deleteByUsername(auth.getName());
        return "redirect:/logout";
    }
}
