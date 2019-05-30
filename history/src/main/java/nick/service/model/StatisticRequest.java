package nick.service.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticRequest {


    private String user_name;

    private Long order_id;

    private String driver_name;

    private String time;

    private String from_address;

    private String to_address;

    private float cost;
}
