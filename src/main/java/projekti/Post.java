package projekti;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post extends AbstractPersistable<Long> {

    private LocalDateTime postTime;
    
    @ManyToOne
    private Account owner;

    private String text;

    @ManyToMany
    private List<Account> likes;

//    @ManyToMany
//    private List<Post> comments;
    
}