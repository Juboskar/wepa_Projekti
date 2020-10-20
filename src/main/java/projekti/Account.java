package projekti;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
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

    @ManyToMany
    private List<Account> friends = new ArrayList<>();

    @ManyToMany
    private List<Account> sended = new ArrayList<>();

    @ManyToMany
    private List<Account> waiting = new ArrayList<>();
    
    @OneToMany(mappedBy="owner")
    private List<Skill> skills = new ArrayList<>();
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] profilepic;
}
