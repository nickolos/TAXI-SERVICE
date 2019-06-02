package nick.service.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment", schema = "payment")
public class Payment {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "cost")
    private float cost;

    @Column(name = "status_payment")
    private boolean status_payment;
}