package be.vdab.restservice.restcontrollers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
//@SpringBootTest maakt alle beans (Controllers, Services, Repositories, ...).
@AutoConfigureMockMvc
//@AutoConfigureMockMvc maakt een object van de class MockMvc. Je stuurt met zo’n object HTTP requests vanuit je test.
@Sql("/insertFiliaal.sql")
class FiliaalControllerIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final MockMvc mockMvc;

    public FiliaalControllerIntegrationTest(MockMvc mvc) {
        this.mockMvc = mvc;
    }

    private long idVanTestFiliaal() {
        return jdbcTemplate.queryForObject("select id from filialen where naam = 'test'", Long.class);
    }

    @Test void onbestaandFiliaalLezen() throws Exception {
        mockMvc.perform(get("/filialen/{id}", -1))
                .andExpect(status().isNotFound());
        //Je stuurt een GET request naar de URI van een niet-bestaand filiaal. De eerste parameter is een URI template.
        //De tweede parameter is de waarde voor de path variabele in de URI template.
    }

    @Test void filiaalLezen() throws Exception {
        mockMvc.perform(get("/filialen/{id}", idVanTestFiliaal()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(idVanTestFiliaal()));
                //Je geeft aan jsonPath een JSONPath expressie mee. Je zoekt daarmee data in JSON data.
    }

}
