package nick.service.controllers;

import nick.service.model.OrderErrorResponse;
import nick.service.service.CannotСreateOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
@ConditionalOnProperty("order.micro.service")
public class OrderExceptionController {
    private static final Logger log = LoggerFactory.getLogger(OrderExceptionController.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CannotСreateOrder.class)
    public OrderErrorResponse notFoundDept(CannotСreateOrder exception) {
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
