/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillDto extends AbstractPersistable<Long> {
    
    private String text;
    private int likes;
    
    private Long identifier;
    
}
