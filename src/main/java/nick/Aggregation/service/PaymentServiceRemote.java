package nick.Aggregation.service;

import nick.service.model.PaymentRequest;
import nick.service.model.PaymentResponse;
import nick.service.service.PaymentNotFoundException;
import nick.service.service.PaymentService;
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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
@Service("remotePaymentService")
public class PaymentServiceRemote implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceRemote.class);
    private static final String paymentOrder = "http://localhost:8083/payment/{id}";

    private static final String paymentChanged = "http://localhost:8083/payment/";
    private static final String piadPayment = "http://localhost:8083/payment/pay/";
    private final RestTemplate rest = new RestTemplate();
    private final BlockingQueue<Long> queue = new LinkedBlockingDeque<>();

    private void throwNotFoundExceptionWhenNeed(HttpClientErrorException e, Long id) {
        if(e.getStatusCode().value() == 404) {
            PaymentNotFoundException ex = new PaymentNotFoundException(id);
            ex.initCause(e);
            throw ex;
        }
    }

    @Scheduled(fixedDelay = 5000)
    public void deletePaymentTask() {
        while(!queue.isEmpty()) {
            try {
                Long id = queue.take();
                try {
                    rest.delete(paymentOrder, id);
                    log.info("Delayed delete payment " + id + " ok");
                } catch (Exception e) {
                    log.info("Delayed delete payment " + id + " failed: " + e.toString());
                    queue.put(id);
                    return;
                }
            } catch (InterruptedException e) {
                log.error("InterruptedException", e);
            }
        }
    }

    @Override
    public void create(Long id, PaymentRequest payment) {
        try {
            String uri = "http://localhost:8083"+ rest.postForLocation(paymentOrder,payment,id);
            log.info(uri);
        } catch (HttpClientErrorException e) {
            throwNotFoundExceptionWhenNeed(e, id);
            log.error("Request error", e);
        } catch (Exception e) {
            log.error("Request error", e);
        }
    }

    @Override
    public PaymentResponse update(Long id, PaymentRequest payment) {
        try {
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

                StringBuilder payload = new StringBuilder();
                payload.append("{ \n\"cost\" : ").append(payment.getCost()).append("\n}");
                log.info(payload.toString());
                HttpPatch httpPatch = new HttpPatch(paymentChanged + id);
                httpPatch.setEntity(new StringEntity(payload.toString(), "application/json", "UTF-8"));
                CloseableHttpResponse c = httpclient.execute(httpPatch);
                log.info(c.toString());
                return null;
            } catch (HttpClientErrorException e) {
                throwNotFoundExceptionWhenNeed(e, id);
                log.error("Request error", e);
            }
        }
        catch (Exception e) {
            log.error("Changed order " + id + " failed");
        }
        return null;
    }

    @Override
    public void to_pay(Long id, PaymentRequest payment) {

        try {
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

                StringBuilder payload = new StringBuilder();
                payload.append("{ \n\"cost\" : ").append(payment.getCost()).append("\n}");
                log.info(payload.toString());
                HttpPatch httpPatch = new HttpPatch(piadPayment + id);
                httpPatch.setEntity(new StringEntity(payload.toString(), "application/json", "UTF-8"));
                CloseableHttpResponse c = httpclient.execute(httpPatch);
                log.info(c.toString());

            } catch (HttpClientErrorException e) {
                throwNotFoundExceptionWhenNeed(e, id);
                log.error("Request error", e);
            }
        }
        catch (Exception e) {
                log.error("Changed order " + id + " failed");
            }
    }



    @Override
    public void delete(Long id) {
        try {
            try {
                rest.delete(paymentOrder, id);
                log.info("Delete payment " + id + " ok");
            } catch (HttpClientErrorException e) {
                throwNotFoundExceptionWhenNeed(e, id);
                log.info("Delete payment " + id + " failed, use delayed delete: " + e.getMessage());
                queue.put(id);
            } catch (Exception e) {
                log.info("Delete payment " + id + " failed, use delayed delete: " + e.getMessage());
                queue.put(id);
            }
        } catch (InterruptedException e) {
            log.error("InterruptedException", e);
        }
    }




}
