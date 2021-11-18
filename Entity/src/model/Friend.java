package model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author sonht
 */

@Entity
@Table(name = "friend")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Friend extends BaseEntity {
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user_1", referencedColumnName = "id")
    private User user_id_1;  // sent
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user_2", referencedColumnName = "id")
    private User user_id_2;  // receive
    
    @Column
    private boolean confirmed;

}
