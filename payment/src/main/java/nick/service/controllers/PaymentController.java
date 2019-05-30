package nick.service.controllers;

import nick.service.model.PaymentRequest;
import nick.service.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@ConditionalOnProperty("payment.micro.service")
@RequestMapping("/payment")
public class PaymentController {
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    @Qualifier("localPaymentService")
    PaymentService paymentService;



    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/{id}")
    public ResponseEntity<?> create(@PathVariable Long id, @RequestBody PaymentRequest payment, HttpServletResponse response) {
        log.info("POST /payment/" + id + " payment " + payment);
        paymentService.create(id, payment);
        response.addHeader(HttpHeaders.LOCATION, "/payment/" + id);
        return ResponseEntity.ok("payment created");
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/pay/{id}")
    public ResponseEntity<?>  pay(@PathVariable Long id, @RequestBody PaymentRequest payment) {
        log.info("PATCH /payment/" + id + " payment " + payment);
        paymentService.to_pay(id, payment);
        return  ResponseEntity.ok("order paid");
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void delete(@PathVariable Long id) {
        log.info("DELETE /payment/" + id);
        paymentService.delete(id);
    }


    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    public ResponseEntity<?>  update (@PathVariable Long id, @RequestBody PaymentRequest payment) {
        log.info("PATCH /payment/" + id);
        paymentService.update(id, payment);
        return  ResponseEntity.ok("order changed");
    }

}
