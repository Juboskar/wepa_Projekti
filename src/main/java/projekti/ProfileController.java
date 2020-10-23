package projekti;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String friends(Model model) {
        model.addAttribute("friends", accountService.findFriends());
        model.addAttribute("waiting", accountService.findWaiting());
        model.addAttribute("sended", accountService.findSended());
        return "friends";
    }

    @GetMapping("/mypage")
    public String mypage(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("path", accountService.findByUsername(username).getUserpath());
        model.addAttribute("skills", accountService.findSkills());
        return "mypage";
    }

    @GetMapping("/kayttajat/{path}")
    public String users(Model model, @PathVariable String path) {
        model.addAttribute("text", "");
        model.addAttribute("path", path);
        model.addAttribute("name", accountService.findNameByPath(path));
        model.addAttribute("skills", accountService.findSkillsByPath(path));
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

    @GetMapping("/deletepicture")
    public String deletePicture() {
        accountService.deleteProfilePicture();
        return "redirect:/mypage";
    }

    @PostMapping("/searchresults")
    public String search(@RequestParam String name, Model model) {
        model.addAttribute("results", accountService.findByName(name));
        return "search";
    }

    @GetMapping("/kayttajat/{path}/sendrequest")
    public String sendFriendRequest(@PathVariable String path, Model model) {
        model.addAttribute("text", accountService.sendRequest(path));
        model.addAttribute("path", path);
        model.addAttribute("name", accountService.findNameByPath(path));
        model.addAttribute("skills", accountService.findSkillsByPath(path));
        return "userpage";
    }

    @GetMapping("/kayttajat/{path}/accept")
    public String acceptFriendRequest(@PathVariable String path) {
        accountService.acceptRequest(path);
        return "redirect:/friends";
    }

    @GetMapping("/kayttajat/{path}/reject")
    public String rejectFriendRequest(@PathVariable String path) {
        accountService.rejectRequest(path);
        return "redirect:/friends";
    }

    @GetMapping("/kayttajat/{path}/remove")
    public String removeFriend(@PathVariable String path) {
        accountService.removeFriend(path);
        return "redirect:/friends";
    }

    @GetMapping("/kayttajat/{path}/cancel")
    public String cancelFriendRequest(@PathVariable String path) {
        accountService.cancelFriend(path);
        return "redirect:/friends";
    }

    @PostMapping("/addskill")
    public String addSkill(@RequestParam String skill) {
        accountService.addSkill(skill);
        return "redirect:/mypage";
    }

    @GetMapping("/mypage/{skill}/remove")
    public String removeSkill(@PathVariable String skill) {
        accountService.removeSkill(skill);
        return "redirect:/mypage";
    }
}
