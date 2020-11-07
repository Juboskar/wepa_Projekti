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
@Transactional
public class AccountServiceFriendsTest {


    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void testSave() throws Exception {
        Account a = new Account();
        a.setUsername("testaaja1");
        a.setPassword(passwordEncoder.encode("Testisalasana123+"));
        a.setName("Testi Henkilo");
        a.setUserpath("testaajayksi");
        accountService.save(a.getUsername(), a.getPassword(), a.getName(), a.getUserpath());
        assertTrue(accountRepository.findAll().contains(a));
    }

    @Test
    public void testSaveExistingAccount() throws Exception {
        Account a = new Account();
        a.setUsername("testaaja2");
        a.setPassword(passwordEncoder.encode("Testisalasana123+"));
        a.setName("Testi Henkilo");
        a.setUserpath("testaajakaksi");
        Account b = new Account();
        b.setUsername("testaaja2");
        b.setPassword(passwordEncoder.encode("Testisalasana123+"));
        b.setName("Testi Henkilo");
        b.setUserpath("testaajakaksi");

        accountService.save(a.getUsername(), a.getPassword(), a.getName(), a.getUserpath());
        accountService.save(b.getUsername(), b.getPassword(), b.getName(), b.getUserpath());

        assertTrue(accountRepository.findAll().size() == 1);
    }

    @Test
    public void testFindByName() {
        Account a = new Account();
        a.setUsername("testaaja4");
        a.setPassword(passwordEncoder.encode("Testisalasana123+"));
        a.setName("Testi Henkilo");
        a.setUserpath("testaajanelja");

        accountRepository.save(a);

        assertTrue(accountService.findByName(a.getName()).size() == 1);
    }

    @Test
    public void testFindFriends() throws Exception {
        Account a = new Account();
        a.setUsername("testaaja5");
        a.setPassword(passwordEncoder.encode("Testisalasana123+"));
        a.setName("Testi Henkilo");
        a.setUserpath("testaajaviisi");
        Account b = new Account();
        b.setUsername("testaaja6");
        b.setPassword(passwordEncoder.encode("Testisalasana123+"));
        b.setName("Testi Henkilo");
        b.setUserpath("testaajakuusi");
        Account c = new Account();
        c.setUsername("testaaja7");
        c.setPassword(passwordEncoder.encode("Testisalasana123+"));
        c.setName("Testi Henkilo");
        c.setUserpath("testaajaseitseman");

        List<Account> friends = new ArrayList<>();
        friends.add(b);
        friends.add(c);
        a.setFriends(friends);
        accountRepository.save(a);
        accountRepository.save(b);
        accountRepository.save(c);

        assertTrue(accountService.findFriends(a.getUsername()).size() == 2);

    }

    @Test
    public void testFindWaiting() throws Exception {
        Account a = new Account();
        a.setUsername("testaaja8");
        a.setPassword(passwordEncoder.encode("Testisalasana123+"));
        a.setName("Testi Henkilo");
        a.setUserpath("testaajakahdeksan");
        Account b = new Account();
        b.setUsername("testaaja9");
        b.setPassword(passwordEncoder.encode("Testisalasana123+"));
        b.setName("Testi Henkilo");
        b.setUserpath("testaajayhdeksan");
        Account c = new Account();
        c.setUsername("testaaja10");
        c.setPassword(passwordEncoder.encode("Testisalasana123+"));
        c.setName("Testi Henkilo");
        c.setUserpath("testaajakymmenen");

        List<Account> waiting = new ArrayList<>();
        waiting.add(b);
        waiting.add(c);
        a.setWaiting(waiting);
        accountRepository.save(a);
        accountRepository.save(b);
        accountRepository.save(c);

        assertTrue(accountService.findWaiting(a.getUsername()).size() == 2);
    }

    @Test
    public void testFindSent() throws Exception {
        Account a = new Account();
        a.setUsername("testaaja11");
        a.setPassword(passwordEncoder.encode("Testisalasana123+"));
        a.setName("Testi Henkilo");
        a.setUserpath("testaajayksitoista");
        Account b = new Account();
        b.setUsername("testaaja12");
        b.setPassword(passwordEncoder.encode("Testisalasana123+"));
        b.setName("Testi Henkilo");
        b.setUserpath("testaajakaksitoista");
        Account c = new Account();
        c.setUsername("testaaja13");
        c.setPassword(passwordEncoder.encode("Testisalasana123+"));
        c.setName("Testi Henkilo");
        c.setUserpath("testaajakolmetoista");

        List<Account> sent = new ArrayList<>();
        sent.add(b);
        sent.add(c);
        a.setSent(sent);
        accountRepository.save(a);
        accountRepository.save(b);
        accountRepository.save(c);

        assertTrue(accountService.findSent(a.getUsername()).size() == 2);
    }

    @Test
    public void testSendRequest() {
        Account a = new Account();
        a.setUsername("testaaja14");
        a.setPassword(passwordEncoder.encode("Testisalasana123+"));
        a.setName("Testi Henkilo");
        a.setUserpath("testaajaneljatoista");
        Account b = new Account();
        b.setUsername("testaaja15");
        b.setPassword(passwordEncoder.encode("Testisalasana123+"));
        b.setName("Testi Henkilo");
        b.setUserpath("testaajaviisitoista");

        accountRepository.save(a);
        accountRepository.save(b);

        assertTrue(accountService.sendRequest(b.getUserpath(), a.getUsername()).equals("Pyyntö lähetetty"));
        assertTrue(accountService.sendRequest(b.getUserpath(), a.getUsername()).equals("Olet jo lähettänyt pyynnön"));
        assertTrue(accountService.sendRequest(a.getUserpath(), b.getUsername()).equals("Käyttäjä on jo lähettänyt sinulle pyynnön. Voit hyväksyä sen kaverisivullasi"));
        assertTrue(accountService.sendRequest(a.getUserpath(), a.getUsername()).equals("Et voi lisätä itseäsi"));

        List<Account> friends = new ArrayList<>();
        friends.add(b);
        a.setFriends(friends);
        accountRepository.save(a);
        assertTrue(accountService.sendRequest(b.getUserpath(), a.getUsername()).equals("Olette jo kavereita"));

    }

    @Test
    public void testAcceptFriendRequest() {
        Account a = new Account();
        a.setUsername("testaaja16");
        a.setPassword(passwordEncoder.encode("Testisalasana123+"));
        a.setName("Testi Henkilo");
        a.setUserpath("testaajakuusitoista");
        Account b = new Account();
        b.setUsername("testaaja17");
        b.setPassword(passwordEncoder.encode("Testisalasana123+"));
        b.setName("Testi Henkilo");
        b.setUserpath("testaajaseitsemantoista");

        List<Account> waiting = new ArrayList<>();
        waiting.add(b);
        a.setWaiting(waiting);

        List<Account> sent = new ArrayList<>();
        sent.add(a);
        b.setSent(sent);

        accountRepository.save(a);
        accountRepository.save(b);

        accountService.acceptRequest(b.getUserpath(), a.getUsername());

        assertTrue(accountService.findFriends(a.getUsername()).size() == 1);
        assertTrue(accountService.findFriends(b.getUsername()).size() == 1);
        assertTrue(accountService.findWaiting(a.getUsername()).isEmpty());
        assertTrue(accountService.findSent(b.getUsername()).isEmpty());
    }

    @Test
    public void testRejectFriendRequest() {
        Account a = new Account();
        a.setUsername("testaaja18");
        a.setPassword(passwordEncoder.encode("Testisalasana123+"));
        a.setName("Testi Henkilo");
        a.setUserpath("testaajakahdeksantoista");
        Account b = new Account();
        b.setUsername("testaaja19");
        b.setPassword(passwordEncoder.encode("Testisalasana123+"));
        b.setName("Testi Henkilo");
        b.setUserpath("testaajayhdeksantoista");

        List<Account> waiting = new ArrayList<>();
        waiting.add(b);
        a.setWaiting(waiting);

        List<Account> sent = new ArrayList<>();
        sent.add(a);
        b.setSent(sent);

        accountRepository.save(a);
        accountRepository.save(b);

        accountService.rejectRequest(b.getUserpath(), a.getUsername());

        assertTrue(accountService.findFriends(a.getUsername()).isEmpty());
        assertTrue(accountService.findFriends(b.getUsername()).isEmpty());
        assertTrue(accountService.findWaiting(a.getUsername()).isEmpty());
        assertTrue(accountService.findSent(b.getUsername()).isEmpty());
    }

    @Test
    public void testRemoveFriend() throws Exception {
        Account a = new Account();
        a.setUsername("testaaja20");
        a.setPassword(passwordEncoder.encode("Testisalasana123+"));
        a.setName("Testi Henkilo");
        a.setUserpath("testaajakaksikymmenta");
        Account b = new Account();
        b.setUsername("testaaja21");
        b.setPassword(passwordEncoder.encode("Testisalasana123+"));
        b.setName("Testi Henkilo");
        b.setUserpath("testaajakaksikymmentayksi");
        Account c = new Account();
        c.setUsername("testaaja22");
        c.setPassword(passwordEncoder.encode("Testisalasana123+"));
        c.setName("Testi Henkilo");
        c.setUserpath("testaajakaksikymmentakaksi");

        List<Account> friends = new ArrayList<>();
        friends.add(b);
        friends.add(c);
        a.setFriends(friends);

        List<Account> friends2 = new ArrayList<>();
        friends2.add(a);
        b.setFriends(friends2);
        List<Account> friends3 = new ArrayList<>();
        friends3.add(a);
        c.setFriends(friends3);

        accountRepository.save(a);
        accountRepository.save(b);
        accountRepository.save(c);

        accountService.removeFriend(b.getUserpath(), a.getUsername());

        assertTrue(accountService.findFriends(a.getUsername()).size() == 1);
        assertTrue(accountService.findFriends(b.getUsername()).isEmpty());
        accountService.removeFriend(c.getUserpath(), a.getUsername());

        assertTrue(accountService.findFriends(a.getUsername()).isEmpty());
        assertTrue(accountService.findFriends(c.getUsername()).isEmpty());
    }

    @Test
    public void testCancelFriend() throws Exception {
        Account a = new Account();
        a.setUsername("testaaja23");
        a.setPassword(passwordEncoder.encode("Testisalasana123+"));
        a.setName("Testi Henkilo");
        a.setUserpath("testaajakaksikymmentakolme");
        Account b = new Account();
        b.setUsername("testaaja24");
        b.setPassword(passwordEncoder.encode("Testisalasana123+"));
        b.setName("Testi Henkilo");
        b.setUserpath("testaajakaksikymmentanelja");
        Account c = new Account();
        c.setUsername("testaaja25");
        c.setPassword(passwordEncoder.encode("Testisalasana123+"));
        c.setName("Testi Henkilo");
        c.setUserpath("testaajakaksikymmentaviisi");

        List<Account> sent = new ArrayList<>();
        sent.add(b);
        sent.add(c);
        a.setSent(sent);

        List<Account> waiting = new ArrayList<>();
        waiting.add(a);
        b.setWaiting(waiting);
        List<Account> waiting2 = new ArrayList<>();
        waiting2.add(a);
        c.setWaiting(waiting2);

        accountRepository.save(a);
        accountRepository.save(b);
        accountRepository.save(c);

        accountService.cancelFriend(b.getUserpath(), a.getUsername());

        assertTrue(accountService.findSent(a.getUsername()).size() == 1);
        assertTrue(accountService.findWaiting(b.getUsername()).isEmpty());

        accountService.cancelFriend(c.getUserpath(), a.getUsername());

        assertTrue(accountService.findSent(a.getUsername()).isEmpty());
        assertTrue(accountService.findWaiting(c.getUsername()).isEmpty());

    }

}
