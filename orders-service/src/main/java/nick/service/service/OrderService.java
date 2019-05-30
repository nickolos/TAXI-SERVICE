package nick.service.service;

import nick.service.model.CloseOrderResponse;
import nick.service.model.OrderRequest;
import nick.service.model.OrderResponse;

import java.util.Map;


public interface OrderService {

    OrderResponse create( OrderRequest order);
    void delete(Long id);
    //OrderResponseStatistics send_statistic (Long id_user);

    CloseOrderResponse update_order_status(Long id_order);


    OrderResponse update_order( Map<Object, Object> updates, Long id);

    OrderResponse find_one (Long id_order);
}
