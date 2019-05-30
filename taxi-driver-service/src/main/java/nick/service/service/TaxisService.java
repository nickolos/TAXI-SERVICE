package nick.service.service;


import nick.service.model.TaxisRequest;
import nick.service.model.TaxisResponse;

public interface TaxisService {

    TaxisResponse appointFreeDriver ();
    TaxisResponse update(Long id, TaxisRequest driver);

}
