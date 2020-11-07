package projekti;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post extends AbstractPersistable<Long> implements Comparable<Post> {

    private LocalDateTime postTime;

    @ManyToOne
    private Account owner;

    @Lob
    @Column(columnDefinition = "TEXT", length = 1000000)
    private String text;

    @ManyToMany
    private List<Account> likes =  new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments  = new ArrayList<>();

    @Override
    public int compareTo(Post p) {
        if (this.getPostTime().isAfter(p.getPostTime())) {
            return -1;
        }
        if (this.getPostTime().isBefore(p.getPostTime())) {
            return 1;
        }
        return 0;
    }
}
