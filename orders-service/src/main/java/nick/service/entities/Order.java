package nick.service.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order", schema = "orders")
public class Order {


   // @GeneratedValue(generator="order", strategy=GenerationType.SEQUENCE)
//    @GenericGenerator(name="hilo-strategy", strategy = "hilo")
//    @G@Id

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @Id
    private Long id;

    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "user_name")
    private String user_name;


    @Column(name = "id_driver")
    private Long id_driver;

    @Column(name = "driver_name")
    private  String driver_name;


    @Column(name = "time")
    private  String time;

    @Column(name = "from_address")
    private String from_address;


    @Column(name = "to_address")
    private String to_address;

    @Column(name = "cost")
    private float cost;

    @Column(name = "order_status")
    private boolean order_status;


}
