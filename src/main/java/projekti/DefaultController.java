package projekti;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @GetMapping("/welcome")
    public String helloWorld(Model model) {
        return "index";
    }

    @GetMapping("*")
    public String redirectToIndex() {
        return "redirect:/welcome";
    }
}
