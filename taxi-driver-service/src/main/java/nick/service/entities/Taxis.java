package nick.service.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "taxis", schema = "card")
public class Taxis {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;


    @Column(name = "car_number")
    private String car_number;

    @Column(name = "phone_number")
    private String phone_number;

    @Column(name = "rating")
    private float rating;


    @Column(name = "status")
    private boolean status;


    public Taxis toDto() {
        return new Taxis(id, name, car_number,phone_number, rating,  status);
    }
}
