package nick.Aggregation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nick.service.model.DetailHistoryResponse;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryLabelResponse {

    private Long order_id;

    private String driver_name;

    private String time;

    private String from_address;

    private String to_address;

    private float cost;

    public HistoryLabelResponse (DetailHistoryResponse historyLabelResponse){


        order_id = historyLabelResponse.getOrder_id();

        driver_name = historyLabelResponse.getDriver_name();

        time = historyLabelResponse.getTime();

        from_address = historyLabelResponse.getFrom_address();

        to_address = historyLabelResponse.getTo_address();

        cost = historyLabelResponse.getCost();
    }

}
