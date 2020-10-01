package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public void deleteByUsername(String username) {
        Account a = accountRepository.findByUsername(username);
        accountRepository.deleteById(a.getId());
    }

    public boolean save(String username, String encodedPassword, String name, String path) {
        if (accountRepository.findByUsername(username) == null) {
            Account a = new Account(username, encodedPassword, name, path);
            accountRepository.save(a);
            return true;
        }
        return false;
    }
}
