package nick.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    private Long user_id;

    private String user_name;

    private Long id_driver;

    private String driver_name;

    private String time;

    private String from_address;

    private String to_address;

 //   private float cost;

 //   private boolean order_status;

}
