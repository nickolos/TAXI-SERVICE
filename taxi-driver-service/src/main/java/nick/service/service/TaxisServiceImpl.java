package nick.service.service;

import nick.service.entities.Taxis;
import nick.service.model.TaxisRequest;
import nick.service.model.TaxisResponse;
import nick.service.repository.TaxisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("localTaxisService")
@ConditionalOnProperty("taxi-driver.micro.service")
public class TaxisServiceImpl implements TaxisService {

    private static final Logger logger = LoggerFactory.getLogger(TaxisServiceImpl.class);

    @Autowired
    private TaxisRepository taxisRepository;


    @Override
    @Transactional
    public TaxisResponse appointFreeDriver() {

        List<Taxis> drivers = new ArrayList<>(getList());

        drivers.addAll(getList());
     //  logger.info(String.valueOf(drivers.size()));
        Taxis taxis=null;
        for (Taxis driver : taxisRepository.findAll()) {

            taxis = driver;
     //       logger.info(taxis.toString());
            if (taxis.isStatus()) {
               // taxisRepository.save(taxis);
                return  new TaxisResponse(taxis);
            }

        }


        if (taxis == null) throw new NoFreeTaxisException();

        return  new TaxisResponse(taxis);
    }



    @Override
    @Transactional
    public TaxisResponse update(Long id, TaxisRequest driver) {

        Taxis entity =  taxisRepository.findById(id).get();
        entity.setStatus(driver.isStatus());
        taxisRepository.save(entity);
        return new TaxisResponse(entity);
    }

    public List<Taxis> getList() {
        return ((List<Taxis>) taxisRepository.findAll())
                .stream()
                .map(Taxis::toDto)
                .collect(Collectors.toList());
    }

}
