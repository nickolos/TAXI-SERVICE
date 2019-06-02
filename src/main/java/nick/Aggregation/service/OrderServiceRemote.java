package nick.Aggregation.service;


import nick.service.model.CloseOrderResponse;
import nick.service.model.OrderErrorResponse;
import nick.service.model.OrderRequest;
import nick.service.model.OrderResponse;
import nick.service.service.OrderService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@Service("remoteOrderService")
public class OrderServiceRemote implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceRemote.class);

    private static final  String idOrder   = "http://localhost:8082/order/{id}";
    
    private static final  String changedOrder  = "http://localhost:8082/order/";

    private static final String createTaxi   = "http://localhost:8082/order/taxi";

  //  private static final String sendStatistic   = "http://localhost:8082/order/statistic/{id_user}";

    private static final String  payment  = "http://localhost:8082/order/payment/";

    private RestTemplate rest = new RestTemplate();


    private final BlockingQueue<Long> queue = new LinkedBlockingDeque<>();

    private final BlockingQueue<Long> queueSend = new LinkedBlockingDeque<>();

    private void throwNotFoundExceptionWhenNeed(HttpClientErrorException e, Long id) {
        if(e.getStatusCode().value() == 404) {
            OrderErrorResponse ex = new OrderErrorResponse(id.toString());
        }
    }





    @Scheduled(fixedDelay = 5000)
    public void deleteTaxiOrder() {
        while(!queue.isEmpty()) {
            try {
                Long id = queue.take();
                try {
                    rest.delete(idOrder, id);
                    log.info("Taxi order cancel " + id + " ok");
                } catch (Exception e) {
                    log.info("Taxi order cancel " + id + " failed: " + e.toString());
                    queue.put(id);
                    return;
                }
            } catch (InterruptedException e) {
                log.error("InterruptedException", e);
            }
        }
    }

    @Override
    public OrderResponse create(OrderRequest order) {

        try {


            OrderResponse response = rest.postForObject(createTaxi, order, OrderResponse.class);

            return response;

        } catch (HttpClientErrorException e) {

            log.error("Request error client", e);
            return null;
        } catch (Exception e) {
            log.error("Request error", e);
            return null;
        }

    }

    @Override
    public void delete(Long id) {

        try {
            try {
                rest.delete(idOrder, id);
                log.info("Taxi order cancel " + id + " ok");
            } catch (HttpClientErrorException e) {
                throwNotFoundExceptionWhenNeed(e,id);
                log.info("Taxi order cancel " + id+ " failed, use delayed delete: " + e.getMessage());
                queue.put(id);
            } catch (Exception e) {
                log.info("Taxi order cancel" + id+ " failed, use delayed delete: " + e.getMessage());
                queue.put(id);
            }
        } catch (InterruptedException e) {
            log.error("InterruptedException", e);
        }

    }


    @Override
    public CloseOrderResponse update_order_status(Long id_order) {
//
//        try {
//            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
//
//
//                HttpPatch httpPatch = new HttpPatch(payment + id_order);
//               // httpPatch.setEntity(new StringEntity(payload.toString(), "application/json", "UTF-8"));
//                CloseableHttpResponse c = httpclient.execute(httpPatch);
//                log.info(c.toString());
//                log.info(c.getAllHeaders().toString());
//                return null;
//            }
//        } catch (Exception e) {
//            log.error("Changed order " + id_order + " failed");
//        }
        return null;
    }

    @Override
    public OrderResponse update_order(Map<Object, Object> updates, Long id) {
        
        try {
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

                StringBuilder payload = new StringBuilder();
                payload.append("{ \n");

                for (Map.Entry<Object, Object> entry : updates.entrySet()) {
                    payload.append("\"");
                    payload.append(entry.getKey().toString());
                    payload.append("\" : \"");
                    payload.append(entry.getValue());
                    payload.append("\",\n");
                }
                payload.replace(payload.lastIndexOf(","),payload.lastIndexOf(",")+1,"");
                payload.append("}");
                log.info(payload.toString());
                HttpPatch httpPatch = new HttpPatch(changedOrder + id);
                httpPatch.setEntity(new StringEntity(payload.toString(), "application/json", "UTF-8"));
                CloseableHttpResponse c = httpclient.execute(httpPatch);
                log.info(c.toString());
                log.info(c.getAllHeaders().toString());
                return null;
            }
        } catch (Exception e) {
            log.error("Changed order " + id + " failed");
        }
        return null;
    }

    @Override
    public OrderResponse find_one(Long id_order) {
        try {
            return rest.getForObject(idOrder, OrderResponse.class, id_order);
        } catch (HttpClientErrorException e) {
            throwNotFoundExceptionWhenNeed(e, id_order);
            log.error("Request error", e);
            return null;
        } catch (Exception e) {
            log.error("Request error", e);
            return null;
        }
    }
}
