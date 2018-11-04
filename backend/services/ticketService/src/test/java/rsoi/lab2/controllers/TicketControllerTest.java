package rsoi.lab2.controllers;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import rsoi.lab2.entity.Ticket;
import rsoi.lab2.model.PingResponse;
import rsoi.lab2.model.TicketInfo;
import rsoi.lab2.services.TicketService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TicketController.class)
public class TicketControllerTest {

    private final Gson gson = new Gson();
    @Autowired
    private MockMvc mvc;
    @MockBean
    private TicketService service;
    @InjectMocks
    private TicketController controller = new TicketController(service);

    @Test
    public void pingTest() {
        assertEquals(controller.ping().getResponse(), new PingResponse("ok").getResponse());
    }

    @Test
    public void getTicketsTest()
            throws Exception {

        TicketInfo ticket = new TicketInfo();
        ticket.setIdTicket(16);
        ticket.setIdFlight(1);
        ticket.setIdPassenger(0);
        ticket.setClassType("ECONOMIC");

        List<TicketInfo> allTickets = Arrays.asList(ticket);

        given(service.listAll()).willReturn(allTickets);

        mvc.perform(get("/tickets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idTicket", is(ticket.getIdTicket())));
    }

    @Test
    public void getTicketTest() throws Exception {
        TicketInfo ticket = new TicketInfo();
        ticket.setIdTicket(16);
        ticket.setIdFlight(1);
        ticket.setIdPassenger(0);
        ticket.setClassType("ECONOMIC");

        given(service.getTicketInfoById(16)).willReturn(ticket);

        MvcResult mvcResult = mvc.perform(get("/ticket")
                .contentType(MediaType.APPLICATION_JSON)
                .param("idTicket", String.valueOf(ticket.getIdTicket())))
                .andExpect(status().isOk())
                .andReturn();

        TicketInfo newTicket = gson.fromJson(mvcResult.getResponse().getContentAsString(), TicketInfo.class);
        assertEquals(ticket.getIdTicket(), newTicket.getIdTicket());
    }

    @Test
    public void getFlightTicketsTest() throws Exception {
        TicketInfo ticket1 = new TicketInfo();
        ticket1.setIdTicket(16);
        ticket1.setIdFlight(5);
        ticket1.setIdPassenger(0);
        ticket1.setClassType("ECONOMIC");

        TicketInfo ticket2 = new TicketInfo();
        ticket2.setIdTicket(16);
        ticket2.setIdFlight(5);
        ticket2.setIdPassenger(0);
        ticket2.setClassType("LUXURY");

        List<TicketInfo> allTickets = Arrays.asList(ticket1, ticket2);
        given(service.listFlightTickets(5)).willReturn(allTickets);

        MvcResult mvcResult = mvc.perform(get("/flightTickets")
                .param("idFlight", "5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JSONArray jsonFlightArray = new JSONArray(mvcResult.getResponse().getContentAsString());
        assertTrue(jsonFlightArray.length() == 2);
    }

    @Test
    public void getFlightTicketsByClassTypeTest() throws Exception {

        given(service.countTicketsByFlightAndClassType(5, "ECONOMIC")).willReturn(1);

        MvcResult mvcResult = mvc.perform(get("/countTickets")
                .param("idFlight", "5")
                .param("classType", "ECONOMIC")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        int count = Integer.parseInt(mvcResult.getResponse().getContentAsString());
        assertTrue(count == 1);
    }

    @Test
    public void addTicketTest() throws Exception {

        TicketInfo ticketInfo = new TicketInfo();
        ticketInfo.setClassType("ECONOMIC");
        ticketInfo.setIdFlight(5);
        ticketInfo.setIdPassenger(1);

        Ticket ticket = new Ticket();
        ticket.setClassType(ticketInfo.getClassType());
        ticket.setIdFlight(ticketInfo.getIdFlight());
        ticket.setIdPassenger(ticketInfo.getIdPassenger());

        given(service.saveOrUpdate(ticket)).willReturn(ticket);

        MvcResult mvcResult = mvc.perform(put("/ticket")
                .content(gson.toJson(ticketInfo))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        int idTicket = Integer.parseInt(mvcResult.getResponse().getContentAsString());

        assertNotEquals(idTicket, -1);
    }

    @Test
    public void editTicketTest() throws Exception {

        TicketInfo ticketInfo = new TicketInfo();
        ticketInfo.setIdTicket(16);
        ticketInfo.setClassType("ECONOMIC");
        ticketInfo.setIdFlight(5);
        ticketInfo.setIdPassenger(1);

        Ticket ticket = new Ticket();
        ticket.setClassType(ticketInfo.getClassType());
        ticket.setIdFlight(ticketInfo.getIdFlight());
        ticket.setIdPassenger(ticketInfo.getIdPassenger());


        given(service.getTicketById(16)).willReturn(ticket);
        given(service.saveOrUpdate(ticket)).willReturn(ticket);

        mvc.perform(patch("/ticket")
                .content(gson.toJson(ticketInfo))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string("Done"));
    }

    @Test
    public void deleteTicketTest() throws Exception {

        doNothing().when(service).delete(16);
        mvc.perform(delete("/ticket")
                .content("16")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string("Done"));
    }

    @Test
    public void deleteFlightTicketsTest() throws Exception {

        doNothing().when(service).deleteFlightTickets(5);
        mvc.perform(delete("/tickets")
                .content("5")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string("Done"));
    }




    /*@TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {

        @Bean
        public TicketService employeeService() {
            return new TicketServiceImpl();
        }
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Mock
    private TicketService service;

    @InjectMocks
    private TicketController controller = new TicketController(service);

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void pingTest(){
        controller.ping();
        assertEquals(controller.ping().getResponse(), new PingResponse("ok").getResponse());
    }


    @Test
    public void listTicketsTest() {
        List<TicketInfo> list = new ArrayList<>();
        when(service.listAll()).thenReturn(list);
        assertNotNull(controller.listTickets());
    }*/


}