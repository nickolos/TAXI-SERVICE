package nick.Aggregation.service;

import nick.service.model.TaxisRequest;
import nick.service.model.TaxisResponse;
import nick.service.service.NoFreeTaxisException;
import nick.service.service.TaxisService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service("remoteTaxiDriverService")
public class TaxisServiceRemote implements TaxisService {
//
//    private static class Help {
//        public Long id;
//        public TaxisRequest taxisRequest;
//
//        public Help(Long id, TaxisRequest taxisRequest) {
//            this.id = id;
//            this.taxisRequest = taxisRequest;
//
//        }
//    }


    private static final Logger log = LoggerFactory.getLogger(TaxisServiceRemote.class);
    private static final String appointDriver = "http://localhost:8081/taxis/appoint";

    private static final String updateddriver = "http://localhost:8081/taxis/";

    private RestTemplate rest = new RestTemplate();


 //   private final BlockingQueue<Help> queue = new LinkedBlockingDeque<>();

    private void throwNotFreeTaxisExceptionNow(HttpClientErrorException e) {
        if (e.getStatusCode().value() == 400) {
            NoFreeTaxisException ex = new NoFreeTaxisException();
            ex.initCause(e);
            throw ex;
        }
    }
//
//
//    @Scheduled(fixedDelay = 5000)
//    public void changedStatusDriver() {
//        while (!queue.isEmpty()) {
//            try {
//                Help taxisRequest = queue.take();
//                taxisRequest.taxisRequest.setStatus(!taxisRequest.taxisRequest.isStatus());
//                try {
//                    rest.patchForObject(updateddriver, taxisRequest, TaxisResponse.class, taxisRequest.id);
//                    log.info("Delayed changed status driver " + taxisRequest.id + " ok");
//                } catch (Exception e) {
//                    log.info("Delayed changed status driver " + taxisRequest.id + " failed: " + e.toString());
//                    queue.put(taxisRequest);
//                    return;
//                }
//            } catch (InterruptedException e) {
//                log.error("InterruptedException", e);
//            }
//        }
//    }


    @Override
    public TaxisResponse appointFreeDriver() {
        try {
            return rest.getForObject(appointDriver, TaxisResponse.class);
        } catch (HttpClientErrorException e) {
            throwNotFreeTaxisExceptionNow(e);
            log.error("Request error, No free taxi", e);
            return null;
        } catch (Exception e) {
            log.info("Request error");
            return null;
        }
    }

    @Override
    public TaxisResponse update(Long id, TaxisRequest driver) {

        try {
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                String payload = "{" + " \"status\" : " + driver.isStatus() + " }";
                log.info(payload);
                HttpPatch httpPatch = new HttpPatch(updateddriver + id);
                httpPatch.setEntity(new StringEntity(payload, "application/json", "UTF-8"));
               CloseableHttpResponse c = httpclient.execute(httpPatch);
               log.info(c.toString());
            }
        } catch (Exception e) {
            log.error("Changed status driver " + id + " failed");
        }
        return null;
    }
}
