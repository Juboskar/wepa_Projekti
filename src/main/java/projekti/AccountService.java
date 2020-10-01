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

    public boolean save(String username, String encodedPassword) {
        if (accountRepository.findByUsername(username) == null) {
            Account a = new Account(username, encodedPassword);
            accountRepository.save(a);
            return true;
        }
        return false;
    }
}
