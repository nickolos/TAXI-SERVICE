package nick.service.controllers;

import nick.service.model.TaxisErrorResponse;
import nick.service.service.NoFreeTaxisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
@ConditionalOnProperty("taxi-driver.micro.service")
public class TaxistExceptionController {
    private static final Logger log = LoggerFactory.getLogger(TaxistExceptionController.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoFreeTaxisException.class)
    public TaxisErrorResponse noFree (NoFreeTaxisException exception) {
        log.error(exception.getMessage(), exception);
        return new TaxisErrorResponse(exception.getMessage());
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public TaxisErrorResponse exception(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new TaxisErrorResponse(exception.getMessage());
    }
}
