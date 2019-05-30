package nick.service.controllers;
import nick.service.model.HistoryListResponse;
import nick.service.model.StatisticRequest;
import nick.service.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@ConditionalOnProperty("history.micro.service")
@RequestMapping("/history")
public class HistoryController {
    private static final Logger log = LoggerFactory.getLogger(HistoryController.class);

    @Autowired
    @Qualifier("localHistoryService")
    HistoryService historyService;


    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/{id}")
    public void create(@PathVariable Long id, @RequestBody StatisticRequest store) {
        log.info("POST /history/" + id );
        historyService.create(id,store);
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public HistoryListResponse findAllOrder(@PathVariable Long id, @RequestParam("page") int page, @RequestParam("size") int size) {
        log.info("GET /id/ page " + page + "size " + size);
        return historyService.findAll(id,page-1, size);
    }
}
