package nick.service.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
//@AllArgsConstructor
@Table(name = "history", schema = "details")

public class DetailHistoryOrder  implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "order_id")
    private Long order_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = " historyUser")
    @JsonManagedReference
    @JsonIgnoreProperties({"hibernateLazyInitializer","detailHistoryOrders"})

    private HistoryUser historyUser;


    @Column(name = "driver_name")
    private String driver_name;


    @Column(name = "time")
    private String time;

    @Column(name = "from_address")
    private String from_address;


    @Column(name = "to_address")
    private String to_address;

    @Column(name = "cost")
    private float cost;


}
