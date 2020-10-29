package projekti;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Transactional
public interface SkillRepository extends JpaRepository<Skill, Long> {

    public Skill findByOwnerAndText(Account a, String s);
    
    public List<Skill> findAllByOwner(Account a);
    
    @Query(value = "Select s from Skill s where s.owner=?1 order by s.likes.size desc")
    public List<Skill> findBySize(Account a, Pageable p);

}
