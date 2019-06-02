package nick.service.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import nick.service.model.TaxisRequest;
import nick.service.repository.TaxisRepository;
import nick.service.service.TaxisService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@WebMvcTest(TaxisController.class)
public class TaxisControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(TaxisControllerTest.class);


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaxisRepository repository;

    @MockBean
    @Qualifier("localTaxisService")
    private TaxisService taxisService;



//
    @Autowired
    private WebApplicationContext context;

//
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

    }

    @Test
    public void appointGetFreeDriver() throws Exception {
        this.mockMvc.perform(get("/taxis/appoint"))
                .andDo(print())
                .andExpect(status().isOk());

    }


    @Test
    public void updatePatch() throws Exception {
        long id = 1;
        TaxisRequest driver = new TaxisRequest();
        driver.setStatus(false);
        String requestJson= new ObjectMapper().writeValueAsString(driver);
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.patch("/taxis/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                   .content(requestJson);
        this.mockMvc.perform(builder)
                .andExpect(status()
                        .isOk())
                .andDo(print());


    }
}