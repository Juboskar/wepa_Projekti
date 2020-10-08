package projekti;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ProfileController {

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
    public String mypage(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("path", accountService.findByUsername(username).getUserpath());
        return "mypage";
    }

    @GetMapping("/kayttajat/{path}")
    public String users(Model model, @PathVariable String path) {
        model.addAttribute("path", path);
        model.addAttribute("name", accountService.findNameByPath(path));
        return "userpage";
    }

    @GetMapping(path = "/kayttajat/{path}/profilepic", produces = "image/*")
    @ResponseBody
    public byte[] get(@PathVariable String path) {
        return accountService.getPicture(path);
    }

    @PostMapping("/files")
    public String save(@RequestParam("file") MultipartFile file) throws IOException {
        accountService.savePicture(file);
        return "redirect:/mypage";
    }
}