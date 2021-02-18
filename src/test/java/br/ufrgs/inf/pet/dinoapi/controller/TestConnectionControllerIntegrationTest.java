package br.ufrgs.inf.pet.dinoapi.controller;

import br.ufrgs.inf.pet.dinoapi.configuration.SpringTestConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringTestConfig.class
)
@TestPropertySource(locations= "classpath:test.properties")
@AutoConfigureMockMvc
public class TestConnectionControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @WithUserDetails("basicuser@dinoapp.com")
    public void isApplicationConnected() throws Exception {
        mvc.perform(
                get("/public/test_connection/")
        ).andExpect(status().isOk());
    }
}
