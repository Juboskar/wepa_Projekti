package projekti;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public String findNameByPath(String userpath) {
        Account a = accountRepository.findByUserpath(userpath);
        return a.getName();
    }

    @Transactional
    public void deleteAccount() {
        //tämä saattaa olla spaghettia mutta toimii ja oli ylimääräinen toiminto jota ei vaadittu!
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account a = accountRepository.findByUsername(username);

        List<Account> aFriends = a.getFriends();
        List<Account> cloned_aFriends = new ArrayList<>(aFriends);
        for (Account b : cloned_aFriends) {
            this.removeFriend(b.getUserpath());
        }

        List<Account> aSended = a.getSended();
        List<Account> cloned_aSended = new ArrayList<>(aSended);
        for (Account b : cloned_aSended) {
            this.cancelFriend(b.getUserpath());
        }

        List<Account> aWaiting = a.getWaiting();
        List<Account> cloned_aWaiting = new ArrayList<>(aWaiting);
        for (Account b : cloned_aWaiting) {
            this.rejectRequest(b.getUserpath());
        }

        List<Skill> aSkills = a.getSkills();
        List<String> strings = new ArrayList<>();
        aSkills.forEach((aSkill) -> {
            strings.add(aSkill.getText());
        });
        for (String string : strings) {
            this.removeSkill(string);
        }

        List<Skill> aLikedSkills = a.getLikedSkills();
        List<Skill> cloned_aLikedSkills = new ArrayList<>(aLikedSkills);
        for (Skill likedSkill : cloned_aLikedSkills) {
            this.dislikeSkill(a.getUsername(), likedSkill.getOwner().getUserpath(), likedSkill.getText());
        }

        List<Post> aPosts = a.getPosts();
        List<Long> postIds = new ArrayList<>();
        aPosts.forEach((aPost) -> {
            postIds.add(aPost.getId());
            aPost.getComments().clear();
        });
        for (Long id : postIds) {
            this.removePost(id);
        }

        List<Post> aLikedPosts = a.getLikedPosts();
        List<Post> cloned_aLikedPosts = new ArrayList<>(aLikedPosts);
        for (Post likedPost : cloned_aLikedPosts) {
            this.dislikePost(a.getUsername(), likedPost.getOwner().getUserpath(), likedPost.getId());
        }

        accountRepository.deleteById(a.getId());
    }

    public boolean save(String username, String encodedPassword, String name, String path) {
        if (accountRepository.findByUsername(username) == null && accountRepository.findByUserpath(path) == null) {
            Account a = new Account();
            a.setUsername(username);
            a.setPassword(encodedPassword);
            a.setName(name);
            a.setUserpath(path);
            accountRepository.save(a);
            return true;
        }
        return false;
    }

    public void savePicture(MultipartFile file) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account a = accountRepository.findByUsername(username);
        if (file.getContentType().equals("image/png") || file.getContentType().equals("image/jpeg")) {
            a.setProfilepic(file.getBytes());
            accountRepository.save(a);
        }
    }

    @Transactional
    public byte[] getPicture(String path) {
        Account a = accountRepository.findByUserpath(path);
        return a.getProfilepic();
    }

    public void deleteProfilePicture() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account a = accountRepository.findByUsername(username);
        a.setProfilepic(null);
        accountRepository.save(a);
    }

    @Transactional
    public List<ProfileDto> findFriends() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account a = accountRepository.findByUsername(username);
        List<ProfileDto> friends = new ArrayList<>();
        a.getFriends().stream().map((b) -> {
            ProfileDto profile = new ProfileDto();
            profile.setName(b.getName());
            profile.setUserpath(b.getUserpath());
            return profile;
        }).forEachOrdered((profile) -> {
            friends.add(profile);
        });
        return friends;
    }

    @Transactional
    public List<ProfileDto> findWaiting() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account a = accountRepository.findByUsername(username);
        List<ProfileDto> waiting = new ArrayList<>();
        a.getWaiting().stream().map((b) -> {
            ProfileDto profile = new ProfileDto();
            profile.setName(b.getName());
            profile.setUserpath(b.getUserpath());
            return profile;
        }).forEachOrdered((profile) -> {
            waiting.add(profile);
        });
        return waiting;
    }

    @Transactional
    public List<ProfileDto> findSended() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account a = accountRepository.findByUsername(username);
        List<ProfileDto> sended = new ArrayList<>();
        a.getSended().stream().map((b) -> {
            ProfileDto profile = new ProfileDto();
            profile.setName(b.getName());
            profile.setUserpath(b.getUserpath());
            return profile;
        }).forEachOrdered((profile) -> {
            sended.add(profile);
        });
        return sended;
    }

    @Transactional
    public List<ProfileDto> findByName(String name) {
        List<Account> accounts = accountRepository.findAllByName(name);
        List<ProfileDto> results = new ArrayList<>();
        accounts.stream().map((a) -> {
            ProfileDto profile = new ProfileDto();
            profile.setName(a.getName());
            profile.setUserpath(a.getUserpath());
            return profile;
        }).forEachOrdered((profile) -> {
            results.add(profile);
        });
        return results;
    }

    @Transactional
    public String sendRequest(String path) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account a = accountRepository.findByUsername(username);
        Account b = accountRepository.findByUserpath(path);
        if (a.getUsername().equals(b.getUsername())) {
            return "Et voi lisätä itseäsi";
        }
        List<Account> aFriends = a.getFriends();
        List<Account> aSended = a.getSended();
        List<Account> aWaiting = a.getWaiting();
        if (aFriends.contains(b)) {
            return "Olette jo kavereita";
        }
        if (aSended.contains(b)) {
            return "Olet jo lähettänyt pyynnön";
        }
        if (aWaiting.contains(b)) {
            return "Käyttäjä on jo lähettänyt sinulle pyynnön. Voit hyväksyä sen kaverisivullasi";
        }
        aSended.add(b);
        a.setSended(aSended);
        List<Account> bWaiting = b.getWaiting();
        bWaiting.add(a);
        b.setWaiting(bWaiting);
        accountRepository.save(a);
        accountRepository.save(b);
        return "Pyyntö lähetetty";
    }

    @Transactional
    public void acceptRequest(String path) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account a = accountRepository.findByUsername(username);
        Account b = accountRepository.findByUserpath(path);

        List<Account> aWaiting = a.getWaiting();
        List<Account> bSended = b.getSended();

        if (aWaiting.contains(b) & bSended.contains(a)) {

            List<Account> aFriends = a.getFriends();
            List<Account> bFriends = b.getFriends();

            aFriends.add(b);
            a.setFriends(aFriends);
            aWaiting.remove(b);
            a.setWaiting(aWaiting);
            accountRepository.save(a);

            bFriends.add(a);
            b.setFriends(bFriends);
            bSended.remove(a);
            b.setSended(bSended);
            accountRepository.save(b);
        }
    }

    @Transactional
    public void rejectRequest(String path) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account a = accountRepository.findByUsername(username);
        Account b = accountRepository.findByUserpath(path);

        List<Account> aWaiting = a.getWaiting();
        List<Account> bSended = b.getSended();

        if (aWaiting.contains(b) & bSended.contains(a)) {

            aWaiting.remove(b);
            a.setWaiting(aWaiting);
            accountRepository.save(a);

            bSended.remove(a);
            b.setSended(bSended);
            accountRepository.save(b);
        }
    }

    @Transactional
    public void removeFriend(String path) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account a = accountRepository.findByUsername(username);
        Account b = accountRepository.findByUserpath(path);

        List<Account> aFriends = a.getFriends();
        List<Account> bFriends = b.getFriends();

        if (aFriends.contains(b) & bFriends.contains(a)) {
            aFriends.remove(b);
            a.setFriends(aFriends);
            accountRepository.save(a);

            bFriends.remove(a);
            b.setFriends(bFriends);
            accountRepository.save(b);
        }
    }

    @Transactional
    public void cancelFriend(String path) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account b = accountRepository.findByUsername(username);
        Account a = accountRepository.findByUserpath(path);

        List<Account> bSended = b.getSended();
        List<Account> aWaiting = a.getWaiting();

        if (aWaiting.contains(b) & bSended.contains(a)) {

            bSended.remove(a);
            b.setSended(bSended);
            accountRepository.save(b);

            aWaiting.remove(b);
            a.setWaiting(aWaiting);
            accountRepository.save(a);
        }
    }

    @Transactional
    public void addSkill(String newSkill) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account a = accountRepository.findByUsername(username);

        List<String> strings = new ArrayList<>();
        List<Skill> aSkills = a.getSkills();
        aSkills.forEach((aSkill) -> {
            strings.add(aSkill.getText());
        });

        if (!strings.contains(newSkill)) {
            Skill skill = new Skill();
            skill.setText(newSkill);
            skill.setOwner(a);
            skillRepository.save(skill);
        }
    }

    @Transactional
    public List<SkillDto> findSkills() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account a = accountRepository.findByUsername(username);

        List<SkillDto> skills = new ArrayList<>();
        List<Skill> aSkills = a.getSkills();
        aSkills.stream().map((s) -> {
            SkillDto skillDto = new SkillDto();
            skillDto.setText(s.getText());
            skillDto.setLikes(s.getLikes().size());
            return skillDto;
        }).forEachOrdered((skillDto) -> {
            skills.add(skillDto);
        });

        return skills;
    }

    @Transactional
    public List<SkillDto> findSkillsByPath(String path) {
        Account a = accountRepository.findByUserpath(path);

        List<SkillDto> skills = new ArrayList<>();
        List<Skill> aSkills = a.getSkills();
        aSkills.stream().map((s) -> {
            SkillDto skillDto = new SkillDto();
            skillDto.setText(s.getText());
            skillDto.setLikes(s.getLikes().size());
            return skillDto;
        }).forEachOrdered((skillDto) -> {
            skills.add(skillDto);
        });

        return skills;
    }

    @Transactional
    public List<SkillDto> findTopSkillsByPath(String path) {
        Account a = accountRepository.findByUserpath(path);

        List<SkillDto> skills = new ArrayList<>();

        Pageable p = PageRequest.of(0, 3);
        List<Skill> aSkills = skillRepository.findBySize(a, p);
        aSkills.stream().map((s) -> {
            SkillDto skillDto = new SkillDto();
            skillDto.setText(s.getText());
            skillDto.setLikes(s.getLikes().size());
            return skillDto;
        }).forEachOrdered((skillDto) -> {
            skills.add(skillDto);
        });

        return skills;
    }

    @Transactional
    public void removeSkill(String skill) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account a = accountRepository.findByUsername(username);
        Skill s = skillRepository.findByOwnerAndText(a, skill);

        List<Account> likes = s.getLikes();
        List<Account> cloned_likes = new ArrayList<>(likes);
        for (Account account : cloned_likes) {

            this.dislikeSkill(account.getUsername(), a.getUserpath(), s.getText());
        }
        s.setLikes(likes);

        skillRepository.delete(s);

    }

    @Transactional
    public void likeSkill(String path, String skill) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account a = accountRepository.findByUsername(username);
        Account b = accountRepository.findByUserpath(path);

        if (!a.equals(b)) {
            Skill s = skillRepository.findByOwnerAndText(b, skill);
            List<Account> likes = s.getLikes();
            if (!likes.contains(a)) {
                likes.add(a);
            }
            s.setLikes(likes);
            skillRepository.save(s);
        }
    }

    @Transactional
    public void dislikeSkill(String account, String path, String skill) {
        Account a = accountRepository.findByUsername(account);
        Account b = accountRepository.findByUserpath(path);

        Skill s = skillRepository.findByOwnerAndText(b, skill);
        List<Account> likes = s.getLikes();
        List<Account> cloned_likes = new ArrayList<>(likes);
        if (cloned_likes.contains(a)) {
            likes.remove(a);
        }
        s.setLikes(likes);
        skillRepository.save(s);
    }

    public void addPost(String newPost) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account a = accountRepository.findByUsername(username);

        Post post = new Post();
        post.setPostTime(LocalDateTime.now());
        post.setText(newPost);
        post.setOwner(a);
        postRepository.save(post);

    }

    @Transactional
    public List<PostDto> findPosts(String path) {
        Account a = accountRepository.findByUserpath(path);

        List<PostDto> posts = new ArrayList<>();
        List<Post> aPosts = a.getPosts();
        aPosts.stream().map((p) -> {
            PostDto postDto = new PostDto();
            postDto.setText(p.getText());
            postDto.setLikes(p.getLikes().size());
            postDto.setLocalDateTime(p.getPostTime());
            postDto.setIdentifier(p.getId());
            return postDto;
        }).forEachOrdered((postDto) -> {
            posts.add(postDto);
        });

        return posts;
    }

    @Transactional
    public void likePost(String path, Long id) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account a = accountRepository.findByUsername(username);
        Account b = accountRepository.findByUserpath(path);

        if (!a.equals(b)) {
            Post p = postRepository.findPostById(id);

            List<Account> likes = p.getLikes();
            if (!likes.contains(a)) {
                likes.add(a);
            }
            p.setLikes(likes);
            postRepository.save(p);
        }
    }

    @Transactional
    public void dislikePost(String account, String path, Long id) {
        Account a = accountRepository.findByUsername(account);
        Account b = accountRepository.findByUserpath(path);

        Post p = postRepository.findPostById(id);
        List<Account> likes = p.getLikes();
        List<Account> cloned_likes = new ArrayList<>(likes);
        if (cloned_likes.contains(a)) {
            likes.remove(a);
        }
        p.setLikes(likes);
        postRepository.save(p);
    }

    @Transactional
    public void removePost(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account a = accountRepository.findByUsername(username);

        Post p = postRepository.findPostById(id);

        List<Account> likes = p.getLikes();
        List<Account> cloned_likes = new ArrayList<>(likes);
        for (Account account : cloned_likes) {
            this.dislikePost(account.getUsername(), a.getUserpath(), p.getId());
        }
        p.setLikes(likes);
        postRepository.delete(p);

    }

    @Transactional
    public List<CommentDto> getComments(Long id) {
        Post p = postRepository.getOne(id);
        List<CommentDto> comments = new ArrayList<>();

        Pageable pageable = PageRequest.of(0, 10);

        List<Comment> commentsBypost = commentRepository.findRecent(p, pageable);
        for (Comment comment : commentsBypost) {
            CommentDto c = new CommentDto();
            c.setText(comment.getText());
            c.setCommentTime(comment.getCommentTime());
            comments.add(c);
        }
        return comments;
    }

    @Transactional
    public PostDto getPostById(Long id) {
        Post p = postRepository.getOne(id);
        PostDto post = new PostDto();
        post.setIdentifier(id);
        post.setText(p.getText());
        return post;
    }

    @Transactional
    public void comment(Long id, String comment) {
        Post p = postRepository.getOne(id);
        Comment c = new Comment();
        c.setCommentTime(LocalDateTime.now());
        c.setText(comment);
        c.setPost(p);
        commentRepository.save(c);
    }
}
