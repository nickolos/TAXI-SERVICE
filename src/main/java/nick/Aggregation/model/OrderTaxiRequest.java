package nick.Aggregation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderTaxiRequest {

    private Long user_id;

    private String user_name;

    private String time;

    private String from_address;

    private String to_address;

}
