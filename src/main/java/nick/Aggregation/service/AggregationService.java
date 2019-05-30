package nick.Aggregation.service;


import nick.Aggregation.model.HistoryLabelListPageResponse;
import nick.Aggregation.model.OrderTaxiRequest;
import nick.service.model.OrderResponse;

import java.util.Map;


public interface AggregationService {


      OrderResponse orderTaxi (OrderTaxiRequest orderTaxiRequest);

      void cancelTaxiOrder ( Long id );

      OrderResponse update_order (Map<Object, Object> updates, Long id );


 //     PaymentResponse to_pay (Long id_order, PaymentRequest paymentRequest);


      HistoryLabelListPageResponse findAllOrderLabel(Long id_user,int page, int size);



}
