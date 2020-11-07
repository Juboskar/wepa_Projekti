package projekti;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServicePostsTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @Transactional
    public void testAddSkill() throws Exception {
        Account a = new Account();
        a.setUsername("TestAAA");
        a.setPassword(passwordEncoder.encode("Testisalasana123+"));
        a.setName("testaaja");
        a.setUserpath("postitestiaaa");
        accountRepository.save(a);

        accountService.addPost("lorem ipsum", a.getUsername());

        assertTrue(postRepository.findPostsByOwner(a).size() == 1);
    }

    @Test
    public void testFindPosts() throws Exception {
        Account a = new Account();
        a.setUsername("TestAAB");
        a.setPassword(passwordEncoder.encode("Testisalasana123+"));
        a.setName("testaaja");
        a.setUserpath("postitestiaab");
        accountRepository.save(a);

        accountService.addPost("lorem ipsum", a.getUsername());
        accountService.addPost("lorem ipsum", a.getUsername());

        assertTrue(accountService.findPosts(a.getUserpath()).size() == 2);
    }

    @Test
    @Transactional
    public void testLikeAndDislikePosts() throws Exception {
        Account a = new Account();
        a.setUsername("TestAAC");
        a.setPassword(passwordEncoder.encode("Testisalasana123+"));
        a.setName("testaaja");
        a.setUserpath("postitestiaac");
        accountRepository.save(a);

        Account b = new Account();
        b.setUsername("TestAAD");
        b.setPassword(passwordEncoder.encode("Testisalasana123+"));
        b.setName("testaaja");
        b.setUserpath("postitestiaad");
        accountRepository.save(b);

        accountService.addPost("lorem ipsum a", a.getUsername());
        accountService.addPost("lorem ipsum b", a.getUsername());

        Long id1 = postRepository.findPostsByOwner(a).get(0).getId();
        Long id2 = postRepository.findPostsByOwner(a).get(0).getId();

        accountService.likePost(b.getUsername(), a.getUserpath(), id1);
        accountService.likePost(b.getUsername(), a.getUserpath(), id2);

        assertTrue(postRepository.findPostById(id1).getLikes().size() == 1);
        assertTrue(postRepository.findPostById(id2).getLikes().size() == 1);

        accountService.dislikePost(b.getUsername(), a.getUserpath(), id1);
        accountService.dislikePost(b.getUsername(), a.getUserpath(), id2);

        assertTrue(postRepository.findPostById(id1).getLikes().isEmpty());
        assertTrue(postRepository.findPostById(id2).getLikes().isEmpty());
    }

}
