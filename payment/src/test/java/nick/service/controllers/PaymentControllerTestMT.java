package nick.service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import junit.framework.TestCase;
import nick.service.PaymentApplication;
import nick.service.entities.Payment;
import nick.service.model.PaymentRequest;
import nick.service.model.PaymentResponse;
import nick.service.service.PaymentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = PaymentApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
public class PaymentControllerTestMT {


    private static final Logger logger = LoggerFactory.getLogger(PaymentControllerTestMT.class);

    private Payment payment = new Payment(1L,(float) 123.9, false);



    private MockMvc mockMvc;

    @MockBean
    private PaymentResponse repository;


    @MockBean
    @Qualifier("localPaymentService")
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController controller;



    //
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    }






    @Test
    public void create() throws Exception {

        Gson gson = new Gson();
        String json = gson.toJson(payment);

        MvcResult result = this.mockMvc.perform(post("/payment/"+payment.getId())
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        logger.info(result.getResponse().getContentAsString());

        logger.info(String.valueOf(result.getResponse().getRedirectedUrl()));

        TestCase.assertEquals("payment created", result.getResponse().getContentAsString());

        assertEquals("/payment/1",result.getResponse().getRedirectedUrl());
    }


    @Test
    public void pay() throws Exception {

        PaymentRequest request = new PaymentRequest();
        request.setCost((float) (payment.getCost()+123.4));
        String requestJson= new ObjectMapper().writeValueAsString(request);

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.patch("/payment/" + payment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson);

        String result =  this.mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals("order changed",result);

    }

    @Test
    public void delete() throws Exception {
        PaymentRequest request = new PaymentRequest();
        request.setCost(payment.getCost());
        String requestJson= new ObjectMapper().writeValueAsString(request);

        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/payment/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());


    }

    @Test
    public void update() throws Exception {
        PaymentRequest request = new PaymentRequest();
        request.setCost(payment.getCost());
        String requestJson= new ObjectMapper().writeValueAsString(request);

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.patch("/payment/" + payment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson);

        String result =  this.mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        assertEquals("order paid",result);

    }
}