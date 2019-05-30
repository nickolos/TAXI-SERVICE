package nick.Aggregation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import nick.Aggregation.AggregationApplication;
import nick.Aggregation.model.AggregationPaginationResponse;
import nick.Aggregation.model.HistoryLabelListPageResponse;
import nick.Aggregation.model.HistoryLabelResponse;
import nick.Aggregation.model.OrderTaxiRequest;
import nick.Aggregation.service.AggregationService;
import nick.service.controllers.HistoryController;
import nick.service.controllers.OrderController;
import nick.service.controllers.PaymentController;
import nick.service.controllers.TaxisController;
import nick.service.entities.Order;
import nick.service.entities.Payment;
import nick.service.model.OrderResponse;
import nick.service.model.PaymentRequest;
import nick.service.model.PaymentResponse;
import nick.service.model.StatisticRequest;
import nick.service.repository.DetailHistoryOrderRepository;
import nick.service.repository.HistoryRepository;
import nick.service.repository.OrderRepository;
import nick.service.repository.TaxisRepository;
import nick.service.service.HistoryService;
import nick.service.service.OrderService;
import nick.service.service.PaymentService;
import nick.service.service.TaxisService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AggregationApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
public class AggregationControllerTestMT {

    private static final Logger logger = LoggerFactory.getLogger(AggregationControllerTestMT.class);

    private MockMvc mockMvc;

    @MockBean
    private DetailHistoryOrderRepository repository;

    @MockBean
    private HistoryRepository repository1;

    @MockBean
  //  @Qualifier("localHistoryService")
    private HistoryService historyService;

    @InjectMocks
    private HistoryController controllerHistiry;


    @MockBean
    private TaxisRepository repositoryTax;


    @MockBean
   // @Qualifier("localHistoryService")
    private TaxisService taxisService;

    @InjectMocks
    private TaxisController controllerTax;

    @MockBean
    private OrderRepository repositoryOrd;


    @MockBean
  //  @Qualifier("localOrderService")
    private OrderService orderService;

    @InjectMocks
    private OrderController controllerOrd;


    @MockBean
    private PaymentResponse repositoryPay;


    @MockBean
   // @Qualifier("localPaymentService")
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController controllerPay;


    @MockBean
    private AggregationService aggregationService;

    @InjectMocks
    private AggregationController controllerAgg;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controllerAgg).build();
        mockMvc = MockMvcBuilders.standaloneSetup(controllerPay).build();
        mockMvc = MockMvcBuilders.standaloneSetup(controllerTax).build();
        mockMvc = MockMvcBuilders.standaloneSetup(controllerOrd).build();
        mockMvc = MockMvcBuilders.standaloneSetup(controllerHistiry).build();


    }


    @Test
    public void orderTaxi() throws Exception {

        Order order = new Order(1L,1L,"Evgen",3L,"Pog","12-11-1997 3-45","Street 12","Street 16",(float) 123.9, false);

        OrderResponse orderResponse = new OrderResponse(order);

        OrderTaxiRequest orderRequest = new  OrderTaxiRequest(1L,"Evgen","12-11-1997 3-45","Street 12","Street 16");


        Mockito.when(aggregationService.orderTaxi(orderRequest)).thenReturn(orderResponse);

        Gson gson = new Gson();
        String json = gson.toJson(orderRequest);
        logger.info(json);
        String orderResponseLson = gson.toJson(orderRequest);

        MvcResult result = this.mockMvc.perform(post("taxi/order/taxi")
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andReturn();
//        logger.info(result.getResponse().getContentAsString());
//
//        logger.info(String.valueOf(result.getResponse().getRedirectedUrl()));


    }

    @Test
    public void cancelTaxiOrder() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("taxi/order/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

    }

    @Test
    public void update_order() throws Exception {
        Order order = new Order(1L,23L,"Evgen",3L,"Pog","12-11-1997 3-45","Street 12","Street 16",(float) 123.9, false);

        Map<Object, Object> fields =new HashMap<>();
        fields.put("time","12-11-1997 3-45");
        fields.put("from_address", "Street 17");
        fields.put("to_address", "Street 170");
        String requestJson= new ObjectMapper().writeValueAsString(fields);

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.patch("taxi/order/" + order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson);

        this.mockMvc.perform(builder)
                .andDo(print())
                .andReturn();
    }

    @Test
    public void to_pay() throws Exception {

        Payment payment = new Payment(1L,(float) 123.9, false);
        PaymentRequest request = new PaymentRequest();
        PaymentResponse paymentResponse = new PaymentResponse(1L,(float) 123.9,false);
        request.setCost((float) (payment.getCost()+123.4));
        String requestJson= new ObjectMapper().writeValueAsString(request);

        Mockito.when(aggregationService.to_pay(1L, request)).thenReturn(paymentResponse);

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.patch("taxi/payment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson);

//        String result =  this.mockMvc.perform(builder)
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andReturn().getResponse().getContentAsString();


    }

    @Test
    public void findAllOrder() throws Exception {
        StatisticRequest statisticRequest = new StatisticRequest("Nick", 1L,"Drive","12-10-17 8-09","Sreeet 12", "Streeet 67", (float) 234.7);
        AggregationPaginationResponse pag = new AggregationPaginationResponse();
        pag.setTotal(1);
        pag.setPages(1);
        pag.setCurrent(1);

        HistoryLabelResponse detail= new HistoryLabelResponse();
        detail.setCost(statisticRequest.getCost());
        detail.setDriver_name(statisticRequest.getDriver_name());
        detail.setFrom_address(statisticRequest.getFrom_address());
        detail.setOrder_id(statisticRequest.getOrder_id());
        detail.setTime(statisticRequest.getTime());
        detail.setTo_address(statisticRequest.getTo_address());

        HistoryLabelListPageResponse list = new HistoryLabelListPageResponse(pag, Collections.singletonList(detail));

        Mockito.when(aggregationService.findAllOrderLabel(1L,1,1)).thenReturn(list);
        MvcResult result = this.mockMvc.perform(get("/history/1?page=1&size=1").accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        result.getResponse().getContentAsString();
        logger.info(result.getResponse().getContentAsString());
    }


}