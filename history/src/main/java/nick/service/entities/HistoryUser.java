package nick.service.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "history", schema = "history")
public class HistoryUser implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "user_name")
    private String user_name;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "historyUser")
    @JsonManagedReference
   // @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnoreProperties( {"hibernateLazyInitializer"})
    List<DetailHistoryOrder> detailHistoryOrders;

}