/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertFalse;
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
public class AccountServiceSkillsTest {

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @Transactional
    public void testAddSkill() throws Exception {
        Account a = new Account();
        a.setUsername("testi0");
        a.setPassword(passwordEncoder.encode("Testisalasana123+"));
        a.setName("Testaaja");
        a.setUserpath("nullnull");

        accountRepository.save(a);
        accountService.addSkill("testiskill", a.getUsername());
        assertTrue(skillRepository.findAllByOwner(a).size() == 1);
    }

    @Test
    public void testFindSkillsByUsername() throws Exception {
        Account a = new Account();
        a.setUsername("test1");
        a.setPassword(passwordEncoder.encode("Testisalasana123+"));
        a.setName("testaaja");
        a.setUserpath("aaaaa");
        accountRepository.save(a);

        Skill s1 = new Skill();
        s1.setText("skill1");
        s1.setOwner(a);
        skillRepository.save(s1);

        Skill s2 = new Skill();
        s2.setText("skill2");
        s2.setOwner(a);
        skillRepository.save(s2);
        assertTrue(accountService.findSkillsByUsername(a.getUsername()).size() == 2);
    }

    @Test
    public void testFindSkillsByPath() throws Exception {
        Account a = new Account();
        a.setUsername("test2");
        a.setPassword(passwordEncoder.encode("Testisalasana123+"));
        a.setName("testaaja");
        a.setUserpath("bbbbb");
        accountRepository.save(a);

        Skill s1 = new Skill();
        s1.setText("skill3");
        s1.setOwner(a);
        skillRepository.save(s1);

        Skill s2 = new Skill();
        s2.setText("skill4");
        s2.setOwner(a);
        skillRepository.save(s2);

        List<SkillDto> list = accountService.findSkillsByPath(a.getUserpath());
        assertTrue(list.size() == 2);
    }

    @Test
    public void testFindTopSkillsByPath() throws Exception {
        Account a = new Account();
        a.setUsername("test3");
        a.setPassword(passwordEncoder.encode("Testisalasana123+"));
        a.setName("testaaja");
        a.setUserpath("ccccc");
        accountRepository.save(a);

        Account b = new Account();
        b.setUsername("test4");
        b.setPassword(passwordEncoder.encode("Testisalasana123+"));
        b.setName("testaaja");
        b.setUserpath("ddddd");
        accountRepository.save(b);

        List<Account> likes = new ArrayList<>();
        likes.add(b);

        Skill s1 = new Skill();
        s1.setText("skill5");
        s1.setOwner(a);
        skillRepository.save(s1);

        Skill s2 = new Skill();
        s2.setText("skill6");
        s2.setOwner(a);
        skillRepository.save(s2);

        Skill s3 = new Skill();
        s3.setText("skill7");
        s3.setOwner(a);
        skillRepository.save(s3);

        Skill s4 = new Skill();
        s4.setText("topskill");
        s4.setLikes(likes);
        s4.setOwner(a);
        skillRepository.save(s4);

        List<SkillDto> list = accountService.findTopSkillsByPath(a.getUserpath());
        assertTrue(list.size() == 3);
        assertTrue(list.get(0).getText().equals("topskill"));
    }

    @Test
    @Transactional
    public void testRemoveSkill() throws Exception {
        Account a = new Account();
        a.setUsername("test5");
        a.setPassword(passwordEncoder.encode("Testisalasana123+"));
        a.setName("testaaja");
        a.setUserpath("eeeee");
        accountRepository.save(a);

        Account b = new Account();
        b.setUsername("test6");
        b.setPassword(passwordEncoder.encode("Testisalasana123+"));
        b.setName("testaaja");
        b.setUserpath("fffff");
        accountRepository.save(b);

        List<Account> likes = new ArrayList<>();
        likes.add(b);

        Skill s1 = new Skill();
        s1.setText("skill8");
        s1.setOwner(a);
        skillRepository.save(s1);

        Skill s2 = new Skill();
        s2.setText("skill9");
        s2.setOwner(a);
        skillRepository.save(s2);

        Skill s3 = new Skill();
        s3.setText("skill10");
        s3.setOwner(a);
        skillRepository.save(s3);

        Skill s4 = new Skill();
        s4.setText("topskill");
        s4.setLikes(likes);
        s4.setOwner(a);
        skillRepository.save(s4);

        Skill s5 = skillRepository.findAllByOwner(a).get(0);
        accountService.removeSkill(s5.getId(), a.getUsername());
        assertTrue(skillRepository.findAllByOwner(a).size() == 3);
    }

    @Test
    @Transactional
    public void testLikeSkill() throws Exception {
        Account a = new Account();
        a.setUsername("test7");
        a.setPassword(passwordEncoder.encode("Testisalasana123+"));
        a.setName("testaaja");
        a.setUserpath("ggggg");
        accountRepository.save(a);

        Account b = new Account();
        b.setUsername("test8");
        b.setPassword(passwordEncoder.encode("Testisalasana123+"));
        b.setName("testaaja");
        b.setUserpath("hhhhh");
        accountRepository.save(b);

        Skill s1 = new Skill();
        s1.setText("skill16");
        s1.setOwner(a);
        skillRepository.save(s1);

        Skill s2 = new Skill();
        s2.setText("skill7");
        s2.setOwner(a);
        skillRepository.save(s2);

        Skill s3 = new Skill();
        s3.setText("skill18");
        s3.setOwner(a);
        skillRepository.save(s3);

        Skill s4 = new Skill();
        s4.setText("skill19");
        s4.setOwner(a);
        skillRepository.save(s4);

        Long id1 = skillRepository.findAllByOwner(a).get(0).getId();
        Long id2 = skillRepository.findAllByOwner(a).get(1).getId();

        accountService.likeSkill(a.getUserpath(), id1, b.getUsername());
        accountService.likeSkill(a.getUserpath(), id2, b.getUsername());

        assertTrue(skillRepository.getOne(id1).getLikes().size() == 1);
        assertTrue(skillRepository.getOne(id2).getLikes().size() == 1);
    }

    @Test
    @Transactional
    public void testDislikeSkill() throws Exception {
        Account a = new Account();
        a.setUsername("test7");
        a.setPassword(passwordEncoder.encode("Testisalasana123+"));
        a.setName("testaaja");
        a.setUserpath("ggggg");
        accountRepository.save(a);

        Account b = new Account();
        b.setUsername("test8");
        b.setPassword(passwordEncoder.encode("Testisalasana123+"));
        b.setName("testaaja");
        b.setUserpath("hhhhh");
        accountRepository.save(b);

        Skill s1 = new Skill();
        s1.setText("skill16");
        s1.setOwner(a);
        skillRepository.save(s1);

        Skill s2 = new Skill();
        s2.setText("skill7");
        s2.setOwner(a);
        skillRepository.save(s2);

        Skill s3 = new Skill();
        s3.setText("skill18");
        s3.setOwner(a);
        skillRepository.save(s3);

        Skill s4 = new Skill();
        s4.setText("skill19");
        s4.setOwner(a);
        skillRepository.save(s4);

        Long id1 = skillRepository.findAllByOwner(a).get(0).getId();
        Long id2 = skillRepository.findAllByOwner(a).get(1).getId();

        List<Account> likes = new ArrayList<>();
        likes.add(b);

        skillRepository.getOne(id1).setLikes(likes);
        skillRepository.getOne(id2).setLikes(likes);

        accountService.dislikeSkill(b.getUsername(), id1);
        accountService.dislikeSkill(b.getUsername(), id2);

        assertTrue(skillRepository.getOne(id1).getLikes().isEmpty());
        assertTrue(skillRepository.getOne(id2).getLikes().isEmpty());
    }
 
}
