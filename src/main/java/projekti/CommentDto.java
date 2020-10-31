
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
public class CommentDto extends AbstractPersistable<Long> {
    
    private String text;
    private LocalDateTime commentTime;
    
}
