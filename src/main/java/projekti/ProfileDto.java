package projekti;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto extends AbstractPersistable<Long> {

    private String name;
    private String userpath;
    
}
