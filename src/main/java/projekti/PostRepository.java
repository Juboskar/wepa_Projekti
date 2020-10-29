package projekti;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, Long> {

    public Post findPostById(Long id);
    
}
