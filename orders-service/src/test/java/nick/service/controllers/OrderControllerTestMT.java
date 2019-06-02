package nick.service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import nick.service.OrderApplication;
import nick.service.entities.Order;
import nick.service.model.OrderRequest;
import nick.service.model.OrderResponse;
import nick.service.repository.OrderRepository;
import nick.service.service.OrderService;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = OrderApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
public class OrderControllerTestMT {

    private static final Logger logger = LoggerFactory.getLogger(OrderControllerTestMT.class);

    private Order order = new Order(1L,23L,"Evgen",3L,"Pog","12-11-1997 3-45","Street 12","Street 16",(float) 123.9, false);



    private MockMvc mockMvc;

    @MockBean
    private OrderRepository repository;


    @MockBean
    @Qualifier("localOrderService")
    private OrderService orderService;

    @InjectMocks
    private OrderController controller;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    }


    @Test
    public void delete() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/order/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void update_order_status() throws Exception {

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.patch("/order/payment/" + order.getId())
                        .contentType(MediaType.APPLICATION_JSON);


        this.mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();


    }

    @Test
    public void update_order() throws Exception {

        Map<Object, Object> fields =new HashMap<>();
        fields.put("time","12-11-1997 3-45");
        fields.put("from_address", "Street 17");
        fields.put("to_address", "Street 170");
        String requestJson= new ObjectMapper().writeValueAsString(fields);

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.patch("/order/" + order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson);

         this.mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();


    }

    @Test
    public void findOne() throws Exception {
        OrderResponse orderResponse = new OrderResponse(order);

        Mockito.when(orderService.find_one(1L)).thenReturn(orderResponse);
        MvcResult result = this.mockMvc.perform(get("/order/1").accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        result.getResponse().getContentAsString();
        logger.info(result.getResponse().getContentAsString());
        Gson gson = new Gson();
       String json = gson.toJson(order);
       assertEquals(json,result.getResponse().getContentAsString());

    }

    @Test
    public void  create() throws Exception {

        OrderResponse orderResponse = new OrderResponse(order);

        OrderRequest orderRequest = new OrderRequest(23L,"Evgen",3L,"Pog","12-11-1997 3-45","Street 12","Street 16");

        Mockito.when(orderService.create(orderRequest)).thenReturn(orderResponse);
        Gson gson = new Gson();
        String json = gson.toJson(orderRequest);
        String orderResponseLson = gson.toJson(orderResponse);

        MvcResult result = this.mockMvc.perform(post("/order/taxi")
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        logger.info(result.getResponse().getContentAsString());

        logger.info(String.valueOf(result.getResponse().getRedirectedUrl()));

        assertEquals("/order/1",result.getResponse().getRedirectedUrl());

        assertEquals(orderResponseLson,result.getResponse().getContentAsString());
    }

}