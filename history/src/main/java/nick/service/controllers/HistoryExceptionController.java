package nick.service.controllers;


import nick.service.model.HistoryErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
@ConditionalOnProperty("history.micro.service")
public class HistoryExceptionController {
    private static final Logger log = LoggerFactory.getLogger(HistoryExceptionController.class);


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public HistoryErrorResponse exception(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new HistoryErrorResponse(exception.getMessage());
    }
}
