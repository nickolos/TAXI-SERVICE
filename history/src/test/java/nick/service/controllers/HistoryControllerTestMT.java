package nick.service.controllers;

import com.google.gson.Gson;
import nick.service.HistoryApplication;
import nick.service.model.DetailHistoryResponse;
import nick.service.model.HistoryListResponse;
import nick.service.model.PaginationResponse;
import nick.service.model.StatisticRequest;
import nick.service.repository.DetailHistoryOrderRepository;
import nick.service.repository.HistoryRepository;
import nick.service.service.HistoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = HistoryApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
public class HistoryControllerTestMT {
    private static final Logger logger = LoggerFactory.getLogger(HistoryControllerTestMT.class);

   // private Order order = new Order(1L,23L,"Evgen",3L,"Pog","12-11-1997 3-45","Street 12","Street 16",(float) 123.9, false);

    private StatisticRequest statisticRequest = new StatisticRequest("Nick", 1L,"Drive","12-10-17 8-09","Sreeet 12", "Streeet 67", (float) 234.7);

    private MockMvc mockMvc;

    @MockBean
    private DetailHistoryOrderRepository repository;

    @MockBean
    private HistoryRepository repository1;

    @MockBean
    @Qualifier("localHistoryService")
    private HistoryService historyService;

    @InjectMocks
    private HistoryController controller;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    }



    @Test
    public void create() throws Exception {

       Gson gson = new Gson();
        String json = gson.toJson(statisticRequest);
        logger.info(json);

        MvcResult result = this.mockMvc.perform(post("/history/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

    }

    @Test
    public void findAllOrder() throws Exception {
        PaginationResponse pag = new PaginationResponse();
        pag.setTotal(1);
        pag.setPages(1);
        pag.setCurrent(1);

        DetailHistoryResponse detail= new DetailHistoryResponse();
        detail.setCost(statisticRequest.getCost());
        detail.setDriver_name(statisticRequest.getDriver_name());
        detail.setFrom_address(statisticRequest.getFrom_address());
        detail.setOrder_id(statisticRequest.getOrder_id());
        detail.setTime(statisticRequest.getTime());
        detail.setTo_address(statisticRequest.getTo_address());

        HistoryListResponse list = new HistoryListResponse(pag, Collections.singletonList(detail));

        Mockito.when(historyService.findAll(1L,1,1)).thenReturn(list);
        MvcResult result = this.mockMvc.perform(get("/history/1?page=1&size=1").accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        result.getResponse().getContentAsString();
        logger.info(result.getResponse().getContentAsString());

    }
}