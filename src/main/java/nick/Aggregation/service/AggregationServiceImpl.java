package nick.Aggregation.service;

import nick.Aggregation.model.HistoryLabelListPageResponse;
import nick.Aggregation.model.OrderTaxiRequest;
import nick.service.model.*;
import nick.service.service.HistoryService;
import nick.service.service.OrderService;
import nick.service.service.TaxisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

@Service
public class AggregationServiceImpl implements AggregationService {
    private static final Logger log = LoggerFactory.getLogger(AggregationServiceImpl.class);

    @Autowired
    @Qualifier("remoteOrderService")
    OrderService orderService;
    @Autowired
    @Qualifier("remoteHistoryService")
    HistoryService historyService;
    @Autowired
    @Qualifier("remoteTaxiDriverService")
    TaxisService taxisDriverService;
//    @Autowired
//    @Qualifier("remotePaymentService")
//    PaymentService paymentService;


    private void checkServiceAvailable(Object response, String service) {
        if(response == null)
            throw new ServiceUnavailableException(service);
    }


    @Override
    public OrderResponse orderTaxi(OrderTaxiRequest orderTaxiRequest)  {

        TaxisResponse driver = taxisDriverService.appointFreeDriver(); //get driver

        //set no free driver
        TaxisRequest taxisRequest = new TaxisRequest();
       // taxisRequest.setId(driver.getId());
        taxisRequest.setStatus(false);
        taxisDriverService.update(driver.getId(),taxisRequest);

        OrderRequest getTaxi = new OrderRequest();
        getTaxi.setUser_id(orderTaxiRequest.getUser_id());
        getTaxi.setUser_name(orderTaxiRequest.getUser_name());
        getTaxi.setId_driver(driver.getId());
        getTaxi.setDriver_name(driver.getName());
        getTaxi.setFrom_address(orderTaxiRequest.getFrom_address());
        getTaxi.setTo_address(orderTaxiRequest.getTo_address());
        getTaxi.setTime(orderTaxiRequest.getTime());

        //
//        //create payment
//        PaymentRequest paymentRequest = new PaymentRequest();
//        paymentRequest.setCost(orderResponse.getCost());
//        paymentService.create(orderResponse.getId(), paymentRequest);
        log.info(orderService.create(getTaxi).toString());


        return  orderService.create(getTaxi);


    }

    @Override
    public void cancelTaxiOrder(Long id) {
      OrderResponse orderResponse =   orderService.find_one(id);
        orderService.delete(id);
        TaxisRequest taxisRequest = new TaxisRequest();
        taxisRequest.setStatus(!taxisRequest.isStatus());
        taxisDriverService.update(orderResponse.getId_driver(),taxisRequest);
      //  paymentService.delete(id);
    }

    @Override
    public OrderResponse update_order(Map<Object, Object> updates, Long id) {


        orderService.update_order(updates, id);
        //create payment
//        PaymentRequest paymentRequest = new PaymentRequest();
//        OrderResponse  order = orderService.find_one(id);
//        paymentRequest.setCost(order.getCost());
//        paymentService.update(order.getId(), paymentRequest);

        return  orderService.find_one(id);
    }

//    @Override
//    public PaymentResponse to_pay(Long id_order, PaymentRequest paymentRequest) {
//
//        paymentService.to_pay(id_order, paymentRequest);
//
//        //CloseOrder
//        orderService.update_order_status(id_order);
//
//        OrderResponse order = orderService.find_one(id_order);
//
//        //changedstatus driver
//        TaxisRequest taxisRequest = new TaxisRequest();
//       // taxisRequest.setId(orderResponse.getId_driver());
//        taxisRequest.setStatus(true);
//        taxisDriverService.update(order.getId_driver(),taxisRequest);
//
//        //send_statistic
//        historyService.create(order.getUser_id(), new StatisticRequest(order.getUser_name(), order.getId(), order.getDriver_name(),
//                order.getTime(),  order.getFrom_address(), order.getTo_address() ,order.getCost()));
//
//
//
//        return null;
//    }

    @Override
    public HistoryLabelListPageResponse findAllOrderLabel(Long id_user, int page, int size) {

        if(page < 0 || size <= 0) {
            throw new PaginationBadArgumentsException();
        }
        HistoryListResponse response = historyService.findAll( id_user,page, size);
        log.info(Arrays.toString(response.getHistory_trip().toArray()));
        checkServiceAvailable(response, "history");
        return new HistoryLabelListPageResponse(response);
    }


}
