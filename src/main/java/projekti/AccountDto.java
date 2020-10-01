package projekti;

import javax.persistence.Entity;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto extends AbstractPersistable<Long> {

    @Size(min = 5, max = 30)
    private String username;
    @ValidPassword
    private String password;
    @Size(min = 5, max = 30)
    private String name;
    @Pattern(regexp="^[a-z]*$",message = "Path must contain only alphabets a-z")
    @Size(min = 5, max = 30)
    private String userpath;
    
}
