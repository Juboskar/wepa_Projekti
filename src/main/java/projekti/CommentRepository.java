
package projekti;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface CommentRepository extends JpaRepository<Comment, Long>{

    public List<Comment> findAllByPost(Post p);
    
    @Query(value = "Select c from Comment c where c.post=?1 order by c.commentTime desc")
    public List<Comment> findRecent(Post p, Pageable pageable);
    
}
