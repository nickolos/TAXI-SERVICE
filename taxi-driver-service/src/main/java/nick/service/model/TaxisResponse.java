package nick.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nick.service.entities.Taxis;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxisResponse {

    private Long id;
    private String name;


    public TaxisResponse(Taxis driver) {
        id = driver.getId();
        name = driver.getName();

    }
}
