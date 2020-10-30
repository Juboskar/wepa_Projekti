package projekti;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface PostRepository extends JpaRepository<Post, Long> {

    public Post findPostById(Long id);
    
//    @Query(value = "Select p from Post p where p.owner=?1 order by p.postTime desc")
//    public List<Post> findFriendsPosts(Account a, Pageable p);
    
}
