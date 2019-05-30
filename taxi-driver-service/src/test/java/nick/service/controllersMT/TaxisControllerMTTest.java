package nick.service.controllersMT;


import com.fasterxml.jackson.databind.ObjectMapper;
import nick.service.TaxisApplication;
import nick.service.controllers.TaxisController;
import nick.service.entities.Taxis;
import nick.service.model.TaxisRequest;
import nick.service.model.TaxisResponse;
import nick.service.repository.TaxisRepository;
import nick.service.service.TaxisService;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TaxisApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
public class TaxisControllerMTTest {

    private static final Logger logger = LoggerFactory.getLogger(TaxisControllerMTTest.class);

    private MockMvc mockMvc;

    @MockBean
    private TaxisRepository repository;


    @MockBean
    @Qualifier("localTaxisService")
    private TaxisService taxisService;

    @InjectMocks
    private TaxisController controller;


    //
//    @Autowired
//    private WebApplicationContext context;


    //
    @Before
    public void setUp() {
      //  jdbcTemplate.setDataSource((DataSource) repository);
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    //    mockMvc = MockMvcBuilders.webAppContextSetup(context).dispatchOptions(true).build();

    }

    @Test
    public void appointFreeDriver() throws Exception {
        Taxis taxist1 = new Taxis(1L,"Jon BO", "837298", "9038734633", (float) 4.7,false);
        Taxis taxist2 = new Taxis(2L,"LHfs", "83729898", "90387346733", (float) 4.1,true);
        List<Taxis> list = new ArrayList<>();
        list.add(taxist1);
        list.add(taxist2);
        TaxisResponse taxisResponse = new TaxisResponse();
        taxisResponse.setId(list.get(1).getId());
        taxisResponse.setName(list.get(1).getName());
        Mockito.when(taxisService.appointFreeDriver()).thenReturn(taxisResponse);

        MvcResult result = this.mockMvc.perform(get("/taxis/appoint").accept("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        result.getResponse().getContentAsString();
        logger.info(result.getResponse().getContentAsString());
        String paload = "{\"id\":"+taxisResponse.getId()+",\"name\":\""+taxisResponse.getName()+"\"}";
        assertEquals(paload,result.getResponse().getContentAsString());
    }


    @Test
    public void update() throws Exception {

        Taxis taxist1 = new Taxis(1L,"Jon BO", "837298", "9038734633", (float) 4.7,false);
        Taxis taxist2 = new Taxis(2L,"LHfs", "83729898", "90387346733", (float) 4.1,true);
        List<Taxis> list = new ArrayList<>();
        list.add(taxist1);
        list.add(taxist2);
        TaxisResponse taxisResponse = new TaxisResponse();
        taxisResponse.setId(list.get(1).getId());
        taxisResponse.setName(list.get(1).getName());
        long id = 2;
        TaxisRequest driver = new TaxisRequest();
        driver.setStatus(false);
        String requestJson= new ObjectMapper().writeValueAsString(driver);
        Mockito.when(taxisService.update(id,driver)).thenReturn(taxisResponse);



        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.patch("/taxis/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson);
        String result =  this.mockMvc.perform(builder)
                .andExpect(status()
                        .isOk())
                 .andReturn().getResponse().getContentAsString();



        logger.info(result);
        assertEquals("driver updated",result);

    }

}
