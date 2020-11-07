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
public class ApplicationController {

    @Autowired
    AccountService accountService;

    @GetMapping("/home")
    public String home(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String path = accountService.findByUsername(username).getUserpath();
        model.addAttribute("posts", accountService.findFriendsPosts());
        model.addAttribute("ownPosts", accountService.findPosts(path));
        return "postpage";
    }

    @GetMapping("/friends")
    public String friends(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("friends", accountService.findFriends(username));
        model.addAttribute("waiting", accountService.findWaiting(username));
        model.addAttribute("sended", accountService.findSent(username));
        return "friends";
    }

    @GetMapping("/mypage")
    public String mypage(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String path = accountService.findByUsername(username).getUserpath();
        model.addAttribute("path", path);
        model.addAttribute("posts", accountService.findPosts(path));
        model.addAttribute("likedPosts", accountService.findLikedPosts(path));
        model.addAttribute("skills", accountService.findSkillsByUsername(username));
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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("text", accountService.sendRequest(path, username));
        model.addAttribute("path", path);
        model.addAttribute("name", accountService.findNameByPath(path));
        model.addAttribute("skills", accountService.findSkillsByPath(path));
        model.addAttribute("topskills", accountService.findTopSkillsByPath(path));
        model.addAttribute("posts", accountService.findPosts(path));
        return "userpage";
    }

    @GetMapping("/kayttajat/{path}/accept")
    public String acceptFriendRequest(@PathVariable String path) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        accountService.acceptRequest(path, username);
        return "redirect:/friends";
    }

    @GetMapping("/kayttajat/{path}/reject")
    public String rejectFriendRequest(@PathVariable String path) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        accountService.rejectRequest(path, username);
        return "redirect:/friends";
    }

    @GetMapping("/kayttajat/{path}/remove")
    public String removeFriend(@PathVariable String path) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        accountService.removeFriend(path, username);
        return "redirect:/friends";
    }

    @GetMapping("/kayttajat/{path}/cancel")
    public String cancelFriendRequest(@PathVariable String path) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        accountService.cancelFriend(path, username);
        return "redirect:/friends";
    }

    @PostMapping("/addskill")
    public String addSkill(@RequestParam String skill) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        accountService.addSkill(skill, username);
        return "redirect:/mypage";
    }

    @GetMapping("/mypage/{id}/remove")
    public String removeSkill(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        accountService.removeSkill(id, username);
        return "redirect:/mypage";
    }

    @GetMapping("/kayttajat/{path}/likeskill/{id}")
    public String likeSkill(@PathVariable String path, @PathVariable Long id, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        accountService.likeSkill(path, id, username);
        return "redirect:/kayttajat/" + path;
    }

    @GetMapping("/kayttajat/{path}/dislikeskill/{id}")
    public String dislikeSkill(@PathVariable String path, @PathVariable Long id, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        accountService.dislikeSkill(username, id);
        return "redirect:/kayttajat/" + path;
    }

    @PostMapping("/addpost")
    public String addPost(@RequestParam String post) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        accountService.addPost(post, username);
        return "redirect:/home";
    }

    @GetMapping("/kayttajat/{path}/likepost/{id}")
    public String likePost(@PathVariable String path, @PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        accountService.likePost(username, path, id);
        return "redirect:/kayttajat/" + path;
    }

    @GetMapping("/mypage/dislikepost/{path}/{id}")
    public String dislikePost(@PathVariable String path, @PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        accountService.dislikePost(username, path, id);
        return "redirect:/mypage";
    }

    @GetMapping("/home/{path}/likepost/{id}")
    public String likePostHomepage(@PathVariable String path, @PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        accountService.likePost(username, path, id);
        return "redirect:/home";
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
