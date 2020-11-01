package projekti;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Transactional
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String username);

    Account findByUserpath(String userpath);

    List<Account> findAllByName(String name);

    
 

}
