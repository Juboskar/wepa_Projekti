package projekti;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbstractPersistable<Long> {

    private String username;
    private String password;
    private String name;
    private String userpath;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] profilepic;

    @OneToMany
    private List<Account> friends = new ArrayList<>();

    @OneToMany
    private List<Account> sended = new ArrayList<>();

    @OneToMany
    private List<Account> waiting = new ArrayList<>();
}
