package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PostsController {

    @Autowired
    AccountService accountService;

    @GetMapping("/home")
    public String home() {
        return "postpage";
    }
    
    @GetMapping("/friends")
    public String friends() {
        return "friends";
    }
    
    @GetMapping("/mypage")
    public String mypage() {
        return "mypage";
    }

    @GetMapping("/kayttajat/{path}")
    public String users(Model model, @PathVariable String path) {
        model.addAttribute("name", accountService.findNameByPath(path));
        return "userpage";
    }
}
