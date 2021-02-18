package br.ufrgs.inf.pet.dinoapi.controller;

import br.ufrgs.inf.pet.dinoapi.configuration.SpringTestConfig;
import br.ufrgs.inf.pet.dinoapi.utils.JsonMapperUtils;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryItemDataModel;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.utils.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringTestConfig.class
)
@TestPropertySource(locations="classpath:test.properties")
@AutoConfigureMockMvc
public class GlossaryControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ClockServiceImpl clockService;

    @Test
    public void saveBasicItem() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title");
        model.setFullText("Mock full text");
        model.setSubtitle("Mock subtitle");
        model.setText("Mock text");
        model.setLastUpdate(clockService.getUTCZonedDateTime());
        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data.text").value("Mock text"));
    }
}
