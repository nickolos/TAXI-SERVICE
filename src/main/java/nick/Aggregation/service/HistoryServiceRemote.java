package nick.Aggregation.service;


import nick.service.model.HistoryListResponse;
import nick.service.model.StatisticRequest;
import nick.service.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Service("remoteHistoryService")
public class HistoryServiceRemote implements HistoryService {

    private static final Logger log = LoggerFactory.getLogger(HistoryServiceRemote.class);
    private static final String storePage = "http://localhost:8084/history/{id}?page={page}&size={size}";
    private static final String createdHistory = "http://localhost:8084/history/{id}";

    private  RestTemplate rest = new RestTemplate();

//
//    @PostConstruct
//    public void fixPostMethod() {
//        rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
//    }


    @Override
    public HistoryListResponse findAll(Long id, int page, int size) {
        try {

//            log.info(String.valueOf(Objects.requireNonNull(rest.getForObject(storePage, HistoryListResponse.class, id, page, size)).getHistory_trip().size()));
           return rest.getForObject(storePage, HistoryListResponse.class, id, page, size);
        } catch (Exception e) {
            log.error("Request error", e);
            return null;
        }
    }


    @Override
    public void create(Long id, StatisticRequest statisticRequest) {
        try {
            rest.postForLocation(createdHistory,statisticRequest, id);
        } catch (HttpClientErrorException e) {
            log.error("Request error", e);
        } catch (Exception e) {
            log.error("Request error", e);
        }

    }

}
