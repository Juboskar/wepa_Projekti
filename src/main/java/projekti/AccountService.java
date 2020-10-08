package projekti;

import java.io.IOException;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public String findNameByPath(String userpath) {
        Account a = accountRepository.findByUserpath(userpath);
        return a.getName();
    }

    public void deleteByUsername(String username) {
        Account a = accountRepository.findByUsername(username);
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

    public byte[] getPicture(String path) {
        Account a = accountRepository.findByUserpath(path);
        return a.getProfilepic();
    }
}
