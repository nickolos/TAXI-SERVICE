package nick.service.controllers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nick.service.entities.Order;
import nick.service.model.CloseOrderResponse;
import nick.service.model.OrderRequest;
import nick.service.model.OrderResponse;
import nick.service.repository.OrderRepository;
import nick.service.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@ConditionalOnProperty("order.micro.service")
@RequestMapping("/order")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);


    @Autowired
    @Qualifier("localOrderService")
    OrderService orderService;

    private OrderRepository orderRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @RequestMapping("json")
    public void json() {
        //get json data from file "taxis.json" in our resources

        File jsonFile = null;
        try {
            jsonFile = ResourceUtils.getFile("classpath:order.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Order> people = objectMapper.readValue(jsonFile, new TypeReference<List<Order>>() {
            });
            orderRepository.saveAll(people);

        } catch (IOException e) {
        }
    }


    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/taxi",consumes= MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderResponse create(@RequestBody OrderRequest order, HttpServletResponse response) {
        logger.info("POST /order/taxi");
        OrderResponse orderResponse =  orderService.create(order);
        response.addHeader(HttpHeaders.LOCATION, "/order/" + orderResponse.getId());
        return  orderResponse;
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void delete(@PathVariable Long id) {
        logger.info("DELETE /order/" + id);
        orderService.delete(id);
    }



    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PATCH, value = "/payment/{id_order}")
    public CloseOrderResponse update_order_status(@PathVariable  Long id_order) {
        logger.info("PATH /order/payment/id_order" + id_order);
        return orderService.update_order_status(id_order);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes= MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderResponse update_order (@PathVariable Long id, @RequestBody Map<Object, Object> fields) {

        return orderService.update_order(fields,id);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public OrderResponse findOne (@PathVariable Long id) {
        logger.info("GET /order/" + id);
       return   orderService.find_one(id);
    }


}