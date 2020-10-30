package projekti;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
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

    @Lob
    @Column(columnDefinition = "TEXT", length=1000000)
    private String text;

    @ManyToMany
    private List<Account> likes;

//    @ManyToMany
//    private List<Comment> comments;
}
