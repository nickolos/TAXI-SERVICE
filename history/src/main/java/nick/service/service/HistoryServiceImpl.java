package nick.service.service;


import nick.service.entities.DetailHistoryOrder;
import nick.service.entities.HistoryUser;
import nick.service.model.DetailHistoryResponse;
import nick.service.model.HistoryListResponse;
import nick.service.model.PaginationResponse;
import nick.service.model.StatisticRequest;
import nick.service.repository.DetailHistoryOrderRepository;
import nick.service.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("localHistoryService")
@ConditionalOnProperty("history.micro.service")
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private DetailHistoryOrderRepository detailHistoryOrderRepository;


    @Override
    @Transactional
    public void create(Long id, StatisticRequest statistic) {

        HistoryUser entity = new HistoryUser();
        entity.setId(id);
        entity.setUser_name(statistic.getUser_name());
        DetailHistoryOrder order = new DetailHistoryOrder();
        order.setOrder_id(statistic.getOrder_id());
        order.setDriver_name(statistic.getDriver_name());
        order.setTime(statistic.getTime());
        order.setFrom_address(statistic.getFrom_address());
        order.setTo_address(statistic.getTo_address());
        order.setCost(statistic.getCost());
        order.setHistoryUser(entity);
        List<DetailHistoryOrder> list = new ArrayList<>();
        list.add(order);
        entity.setDetailHistoryOrders(list);
        detailHistoryOrderRepository.save(order);
        historyRepository.save(entity);
    }


    @Override
    @Transactional(readOnly = true)
    public HistoryListResponse findAll(Long id, int page, int size) {

        HistoryListResponse result = new HistoryListResponse();
        HistoryUser user = historyRepository.getOne(id);


        result.setPagination(new PaginationResponse(historyRepository.getOne(id).getDetailHistoryOrders().size(), page, size));


        List<DetailHistoryResponse> list = new ArrayList<>();
        for (DetailHistoryResponse detailHistoryOrder : detailHistoryOrderRepository.findAllByHistoryUser(user, new PageRequest(page, size))) {
            DetailHistoryResponse detailHistoryResponse = new DetailHistoryResponse(detailHistoryOrder);
            list.add(detailHistoryResponse);
        }
        result.setHistory_trip(list);

        return result;
    }
}
