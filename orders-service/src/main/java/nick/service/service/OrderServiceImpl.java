package nick.service.service;

import nick.service.entities.Order;
import nick.service.model.CloseOrderResponse;
import nick.service.model.OrderRequest;
import nick.service.model.OrderResponse;
import nick.service.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Random;


@Service("localOrderService")
@ConditionalOnProperty("order.micro.service")
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private OrderRepository orderRepository;

    private Order findChecked(Long id) {
        Order entity = orderRepository.findById(id).get();
        if(entity == null) {
            throw new CannotСreateOrder();
        }
        return entity;
    }

    @Transactional
     float getCost() {
        Random random = new Random();
        int MIN_COST =100;
        int MAX_COST =2000;
        float cost =(float) MIN_COST + random.nextFloat() * ( MAX_COST - MIN_COST);

        return new BigDecimal(cost).setScale(2, RoundingMode.HALF_UP).floatValue();

    }


    @Override
    @Transactional
    public OrderResponse create(OrderRequest order) {

        Order entity = new Order();
        entity.setUser_id(order.getUser_id());
        entity.setUser_name(order.getUser_name());
        entity.setId_driver(order.getId_driver());
        entity.setDriver_name(order.getDriver_name());
        entity.setTime(String.valueOf(order.getTime()));
        entity.setFrom_address(order.getFrom_address());
        entity.setTo_address(order.getTo_address());
        entity.setCost(getCost());
        entity.setOrder_status(false);
        entity = orderRepository.save(entity);
        return new OrderResponse(entity);
    }


    @Override
    @Transactional
    public void delete(Long id) {
        orderRepository.delete(findChecked(id));

    }


    @Override
    @Transactional
    public CloseOrderResponse update_order_status(Long id_order) {
        Order entity = orderRepository.findById(id_order).get();

        entity.setOrder_status(true);

        return new CloseOrderResponse(id_order);

    }

    @Override
    @Transactional
    public OrderResponse update_order(  Map<Object, Object> updates, Long id) {

        Order order = orderRepository.findById(id).get();
        updates.forEach((k, v) -> {
            // use reflection to get field k on manager and set it to value k
            Field field =  ReflectionUtils.findField(Order.class, k.toString());
            assert field != null;
            field.setAccessible(true);
            ReflectionUtils.makeAccessible(field);
            //field.;
           // logger.info( field.getGenericType().toString());
            ReflectionUtils.setField(field, order, v);

        });
        order.setCost(getCost());
        orderRepository.save(order);
        return new  OrderResponse(findChecked(id));
    }

    @Override
    @Transactional
    public OrderResponse find_one(Long id_order) {

        Order entity = orderRepository.findById(id_order).get();

        return new OrderResponse(entity);
    }

}
