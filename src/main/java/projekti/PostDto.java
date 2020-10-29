package projekti;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto extends AbstractPersistable<Long> {
    
    private String text;
    private int likes;
    private long identifier;
    private LocalDateTime localDateTime;
    
}