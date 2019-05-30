package nick.Aggregation.controllers;

import nick.Aggregation.service.PaginationBadArgumentsException;
import nick.Aggregation.service.PostRequiredFieldException;
import nick.Aggregation.service.ServiceUnavailableException;
import nick.service.controllers.OrderExceptionController;
import nick.service.model.HistoryErrorResponse;
import nick.service.model.OrderErrorResponse;
import nick.service.model.TaxisErrorResponse;
import nick.service.service.NoFreeTaxisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class AggregationExceptionController {
    private static final Logger log = LoggerFactory.getLogger(OrderExceptionController.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(HistoryErrorResponse.class)
    public HistoryErrorResponse noFreeDriver (HistoryErrorResponse exception) {
        log.error(exception.getMessage(), exception);
        return new HistoryErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class,
            PaginationBadArgumentsException.class,
            PostRequiredFieldException.class})
    public TaxisErrorResponse noFreeDriver (NoFreeTaxisException exception) {
        log.error(exception.getMessage(), exception);
        return new TaxisErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ServiceUnavailableException.class)
    public OrderErrorResponse serviceUnavailable(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new OrderErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public OrderErrorResponse exception(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new OrderErrorResponse(exception.getMessage());
    }
}
