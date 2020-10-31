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
        String path = accountService.findByUsername(username).getUserpath();
        model.addAttribute("path", path);
        model.addAttribute("posts", accountService.findPosts(path));
        model.addAttribute("skills", accountService.findSkills());
        return "mypage";
    }

    @GetMapping("/kayttajat/{path}")
    public String users(Model model, @PathVariable String path) {
        model.addAttribute("text", "");
        model.addAttribute("path", path);
        model.addAttribute("name", accountService.findNameByPath(path));
        model.addAttribute("skills", accountService.findSkillsByPath(path));
        model.addAttribute("topskills", accountService.findTopSkillsByPath(path));
        model.addAttribute("posts", accountService.findPosts(path));
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
        model.addAttribute("topskills", accountService.findTopSkillsByPath(path));
        model.addAttribute("posts", accountService.findPosts(path));
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

    @GetMapping("/kayttajat/{path}/likeskill/{skill}")
    public String likeSkill(@PathVariable String path, @PathVariable String skill, Model model) {
        accountService.likeSkill(path, skill);
        return "redirect:/kayttajat/" + path;
    }

    @GetMapping("/kayttajat/{path}/dislikeskill/{skill}")
    public String dislikeSkill(@PathVariable String path, @PathVariable String skill, Model model) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        accountService.dislikeSkill(username, path, skill);
        return "redirect:/kayttajat/" + path;
    }

    @PostMapping("/addpost")
    public String addPost(@RequestParam String post) {
        accountService.addPost(post);
        return "redirect:/home";
    }

    @GetMapping("/kayttajat/{path}/likepost/{id}")
    public String likePost(@PathVariable String path, @PathVariable Long id) {
        accountService.likePost(path, id);
        return "redirect:/kayttajat/" + path;
    }

    @GetMapping("/kayttajat/{path}/dislikepost/{id}")
    public String dislikePost(@PathVariable String path, @PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        accountService.dislikePost(username, path, id);
        return "redirect:/kayttajat/" + path;
    }

    @GetMapping("/mypage/removepost/{id}")
    public String removePost(@PathVariable Long id) {
        accountService.removePost(id);
        return "redirect:/mypage";
    }

    @GetMapping("/post/{id}")
    public String showPost(@PathVariable Long id, Model model) {
        model.addAttribute("post", accountService.getPostById(id));
        model.addAttribute("comments", accountService.getComments(id));
        return "post";
    }
    
    @PostMapping("/post/{id}/comment")
    public String commentPost(@PathVariable Long id, @RequestParam String comment) {
        accountService.comment(id, comment);
        
        return "redirect:/post/" + id;
    }
}
