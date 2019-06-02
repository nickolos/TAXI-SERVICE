package nick.Aggregation.controllers;

import nick.Aggregation.model.HistoryLabelListPageResponse;
import nick.Aggregation.model.OrderTaxiRequest;
import nick.Aggregation.service.AggregationService;
import nick.service.model.OrderResponse;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@RestController
//@CrossOrigin(origins = "http://localhost:8080")
@CrossOrigin(origins = "*")
//@RequestMapping()
public class AggregationController {
    private static final Logger logger = LoggerFactory.getLogger(AggregationController.class);

//    @GetMapping("/")
//    public void greeting() {
//
//    }
//
//    @GetMapping("/current")
//    public void taxi() {
//
//    }
//
//    @GetMapping("/history")
//    public void history() {
//
//    }


    @Autowired
    private AggregationService aggregationService;

   // @CrossOrigin(origins = "http://localhost:8085")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/api/neworder", consumes =MediaType.APPLICATION_JSON_VALUE)
    public OrderResponse orderTaxi (@RequestBody OrderTaxiRequest order, HttpServletResponse response) {
        logger.info("POST /order/taxi");
        OrderResponse orderResponse =  aggregationService.orderTaxi(order);
        response.addHeader(HttpHeaders.LOCATION, "/api/order/"+orderResponse.getId());
        //response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,"http://localhost:8085");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, PATCH, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        return orderResponse;
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(method = RequestMethod.DELETE, value = "/api/order/{id}")
    public void cancelTaxiOrder (@PathVariable Long id) {
        logger.info("DELETE /api/order/" + id);
        aggregationService.cancelTaxiOrder(id);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PATCH, value = "api/order/{id}",consumes= MediaType.APPLICATION_JSON_VALUE)
    public OrderResponse update_order (@PathVariable Long id, @RequestBody Map<Object, Object> fields) {
        logger.info("PATCH /api/order/" + id );
        OrderResponse orderResponse = aggregationService.update_order(fields, id);
        return orderResponse;
    }

//    @ResponseBody
//    @RequestMapping(method = RequestMethod.PATCH, value = "/payment/{id}",consumes= MediaType.APPLICATION_JSON_VALUE)
//    public PaymentResponse to_pay (@PathVariable Long id, @Valid @RequestBody PaymentRequest payment) {
//        logger.info("PATCH /payment/" + id );
//        return aggregationService.to_pay(id, payment);
//    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "api/history/{id_user}")
    public HistoryLabelListPageResponse findAllOrder (@PathVariable Long id_user,@Valid @RequestParam("page") int page, @Valid @RequestParam("size") int size) {
        logger.info("GET /" + id_user + "?page=" + page + "&size="  + size);
        return aggregationService.findAllOrderLabel(id_user, page, size);
    }


}
