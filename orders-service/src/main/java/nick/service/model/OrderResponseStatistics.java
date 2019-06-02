package nick.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import nick.service.entities.Order;


@Data
@AllArgsConstructor
public class OrderResponseStatistics {

    private Long id;

    private Long user_id;

    private String user_name;

    private String driver_name;

    private String time;

    private String from_address;

    private String to_address;

    private float cost;

    public OrderResponseStatistics (Order order){

        id = order.getId();

        user_id = order.getUser_id();

        user_name = order.getUser_name();

        driver_name = order.getDriver_name();

        time = order.getTime();

        from_address =order.getFrom_address();

        to_address = order.getTo_address();

        cost = order.getCost();

    }

}
