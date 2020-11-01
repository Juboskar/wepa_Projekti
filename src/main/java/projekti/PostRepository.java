package projekti;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Transactional
public interface PostRepository extends JpaRepository<Post, Long> {

    public Post findPostById(Long id);

    public List<Post> findPostsByOwner(Account account);

}
