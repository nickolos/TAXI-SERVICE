package nick.service.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import nick.service.entities.DetailHistoryOrder;

@Data
@NoArgsConstructor
public class DetailHistoryResponse {


    private Long order_id;

    private String driver_name;

    private String time;

    private String from_address;

    private String to_address;

    private float cost;


    public DetailHistoryResponse(DetailHistoryOrder detailHistoryOrder) {


        order_id = detailHistoryOrder.getOrder_id();

        driver_name = detailHistoryOrder.getDriver_name();

        time = detailHistoryOrder.getTime();

        from_address = detailHistoryOrder.getFrom_address();

        to_address = detailHistoryOrder.getTo_address();

        cost = detailHistoryOrder.getCost();
    }

    public DetailHistoryResponse(DetailHistoryResponse historyResponse) {

        //    public DetailHistoryResponse(HistoryDetail historyDetail) {
        order_id = historyResponse.getOrder_id();

        driver_name = historyResponse.getDriver_name();

        time =historyResponse.getTime();

        from_address = historyResponse.getFrom_address();

        to_address = historyResponse.getTo_address();

        cost = historyResponse.getCost();
//
    }

}