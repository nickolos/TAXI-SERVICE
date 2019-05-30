package nick.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nick.service.entities.Order;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;

    private Long user_id;

    private String user_name;

    private Long id_driver;

    private String driver_name;

    private String time;

    private String from_address;

    private String to_address;

    private float cost;

    private boolean order_status;

    public OrderResponse (Order order){
        id = order.getId();

        user_id = order.getUser_id();

        user_name = order.getUser_name();

        id_driver = order.getId_driver();

        driver_name = order.getDriver_name();

        time = order.getTime();

        from_address =order.getFrom_address();

        to_address = order.getTo_address();

        cost = order.getCost();

        order_status = order.isOrder_status();
    }
}
