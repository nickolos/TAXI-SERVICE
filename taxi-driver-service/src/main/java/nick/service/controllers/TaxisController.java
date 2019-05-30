package nick.service.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nick.service.entities.Taxis;
import nick.service.model.TaxisRequest;
import nick.service.model.TaxisResponse;
import nick.service.repository.TaxisRepository;
import nick.service.service.TaxisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@ConditionalOnProperty("taxi-driver.micro.service")
@RequestMapping("/taxis")
public class TaxisController  {
    private static final Logger logger = LoggerFactory.getLogger(TaxisController.class);

    @Autowired
    private TaxisRepository taxisRepository;

    @Autowired
    @Qualifier("localTaxisService")
    TaxisService taxisService;


    @RequestMapping("json")
    public void json() {
        //get json data from file "taxis.json" in our resources

        File jsonFile = null;
        try {
            jsonFile = ResourceUtils.getFile("classpath:taxis.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Taxis> people = objectMapper.readValue(jsonFile, new TypeReference<List<Taxis>>() {
            });

            taxisRepository.saveAll(people);

            logger.info("All records saved.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/appoint")
    public TaxisResponse  appointFreeDriver( ) {
        logger.info("GET /taxi/appoint" );
        return taxisService.appointFreeDriver();
    }

    @ResponseBody
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody TaxisRequest driver) {
        logger.info("PATH /taxi/" + id);
        taxisService.update(id, driver);
        return ResponseEntity.ok("driver updated");
    }

}
