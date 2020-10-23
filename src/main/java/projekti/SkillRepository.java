
package projekti;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface SkillRepository extends JpaRepository<Skill, Long> {

    public Skill findByOwnerAndText(Account a, String skill);
    
}
