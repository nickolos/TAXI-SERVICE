package nick.service.service;


import nick.service.model.HistoryListResponse;
import nick.service.model.StatisticRequest;

public interface HistoryService {
  HistoryListResponse findAll(Long id, int page, int size);

   void create (Long id, StatisticRequest statisticRequest);

}
