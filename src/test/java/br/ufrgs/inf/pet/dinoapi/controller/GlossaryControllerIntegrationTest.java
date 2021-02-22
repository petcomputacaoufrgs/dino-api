package br.ufrgs.inf.pet.dinoapi.controller;

import br.ufrgs.inf.pet.dinoapi.configuration.SpringTestConfig;
import br.ufrgs.inf.pet.dinoapi.controller.glossary.GlossaryControllerImpl;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.*;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.SynchronizableDataResponseModelImpl;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringTestConfig.class
)
@TestPropertySource(locations="classpath:test.properties")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GlossaryControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ClockServiceImpl clockService;

    @Autowired
    private GlossaryControllerImpl glossaryController;

    private static String generateExistsPattern(GlossaryItemDataModel model) {
        final ZonedDateTime lastUpdate = model.getLastUpdate();
        final String title = model.getTitle();
        final String text = model.getText();
        final String subtitle = model.getSubtitle();
        final String fullText = model.getFullText();
        return "$.data[?(@.lastUpdate=~/^"+lastUpdate.toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)/i"+" && @.title=="+(title != null ? "'"+title+"'": "null")+" && @.text=="+(text != null ? "'"+text+"'": "null")+" && @.fullText=="+(fullText != null ? "'"+fullText+"'": "null")+" && @.subtitle=="+(subtitle != null ? "'"+subtitle+"'": "null")+")]";
    }

    private Long saveItem(GlossaryItemDataModel model) throws Exception {
        final ResponseEntity<SynchronizableDataResponseModelImpl<Long, GlossaryItemDataModel>> result = glossaryController.save(model);
        if (result.getBody() != null && result.getBody().getData() != null && result.getBody().getData().getId() != null) {
            return result.getBody().getData().getId();
        }

        throw new Exception("Fail to save GlossaryItemDataModel on API");
    }

    //<editor-fold desc="Save item">
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
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+model.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data.text").value("Mock text"));
    }

    @Test
    public void trySaveItemWithoutTitle() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setFullText("Mock full text");
        model.setSubtitle("Mock subtitle");
        model.setText("Mock text");
        model.setLastUpdate(clockService.getUTCZonedDateTime());
        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveItemWithoutFullText() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title");
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
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+model.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data.text").value("Mock text"));
    }

    @Test
    public void saveItemWithoutSubtitle() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title");
        model.setFullText("Mock full text");
        model.setText("Mock text");
        model.setLastUpdate(clockService.getUTCZonedDateTime());
        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+model.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data.subtitle").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.text").value("Mock text"));
    }

    @Test
    public void saveItemWithoutText() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title");
        model.setFullText("Mock full text");
        model.setSubtitle("Mock subtitle");
        model.setLastUpdate(clockService.getUTCZonedDateTime());
        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void trySaveItemWithoutLastUpdate() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title");
        model.setFullText("Mock full text");
        model.setSubtitle("Mock subtitle");
        model.setText("Mock text");
        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void trySaveItemWithTitleLessThanMin() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("");
        model.setFullText("Mock full text");
        model.setSubtitle("Mock subtitle");
        model.setText("Mock text");
        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveItemWithTitleWithMixSize() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("M");
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
                .andExpect(jsonPath("$.data.title").value("M"))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+model.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data.text").value("Mock text"));
    }

    @Test
    public void saveItemWithTitleWithMaxSize() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title Mock title Mock title Mock title eeeeee");
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
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+model.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data.title").value("Mock title Mock title Mock title Mock title eeeeee"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data.text").value("Mock text"));
    }

    @Test
    public void trySaveItemWithTitleGreaterThanMax() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title Mock title Mock title Mock title eeeeeea");
        model.setFullText("Mock full text");
        model.setSubtitle("Mock subtitle");
        model.setText("Mock text");
        model.setLastUpdate(clockService.getUTCZonedDateTime());
        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveItemWithTextEqualThanMax() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title");
        model.setFullText("Mock full text");
        model.setSubtitle("Mock subtitle");
        model.setText("Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee eawkepkqiowekoqwieoqwjeoijqwoejqwojeoijiqoiweoqjwoiqwieowowjoqwjoiqwqoejwoeqooqiwo");
        model.setLastUpdate(clockService.getUTCZonedDateTime());
        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+model.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data.text").value("Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee eawkepkqiowekoqwieoqwjeoijqwoejqwojeoijiqoiweoqjwoiqwieowowjoqwjoiqwqoejwoeqooqiwo"));
    }

    @Test
    public void trySaveItemWithTextGreaterThanMax() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title");
        model.setFullText("Mock full text");
        model.setSubtitle("Mock subtitle");
        model.setText("Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee Mock title Mock title Mock title Mock title eeeeee eawkepkqiowekoqwieoqwjeoijqwoejqwojeoijiqoiweoqjwoiqwieowowjoqwjoiqwqoejwoeqooqiweo");
        model.setLastUpdate(clockService.getUTCZonedDateTime());
        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveItemWitSubtitleEqualToMax() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title");
        model.setFullText("Mock full text");
        model.setSubtitle("Mock subtitle mock m");
        model.setText("Mock text");
        model.setLastUpdate(clockService.getUTCZonedDateTime());
        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+model.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle mock m"))
                .andExpect(jsonPath("$.data.text").value("Mock text"));
    }

    @Test
    public void trySaveItemWitSubtitleGreaterThanMax() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title");
        model.setFullText("Mock full text");
        model.setSubtitle("Mock subtitle mock mo");
        model.setText("Mock text");
        model.setLastUpdate(clockService.getUTCZonedDateTime());
        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveItemWithFullTextEqualThanMax() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title");
        model.setFullText("Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full ");
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
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+model.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full "))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data.text").value("Mock text"));
    }

    @Test
    public void trySaveItemWithFullTextEqualThanMax() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title");
        model.setFullText("Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full Mock full text Mock full text Mock full text Mock full text Mock full text Mock full text Mock full m");
        model.setSubtitle("Mock subtitle");
        model.setText("Mock text");
        model.setLastUpdate(clockService.getUTCZonedDateTime());
        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void trySaveItemWithTitleNull() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setFullText("Mock fulltext");
        model.setSubtitle("Mock subtitle");
        model.setText("Mock text");
        model.setLastUpdate(clockService.getUTCZonedDateTime());
        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void trySaveItemWithTextNull() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title");
        model.setFullText("Mock fulltext");
        model.setSubtitle("Mock subtitle");
        model.setText(null);
        model.setLastUpdate(clockService.getUTCZonedDateTime());
        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveItemWithSubtitleNull() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title");
        model.setFullText("Mock full text");
        model.setSubtitle(null);
        model.setText("Mock text");
        model.setLastUpdate(clockService.getUTCZonedDateTime());
        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+model.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data.subtitle").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.text").value("Mock text"));
    }

    @Test
    public void saveItemWithFullTextNull() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title");
        model.setFullText(null);
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
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+model.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data.text").value("Mock text"));
    }

    @Test
    public void saveItemWithAllPossibleFieldsNull() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title");
        model.setFullText(null);
        model.setSubtitle(null);
        model.setText("Mock text");
        model.setLastUpdate(clockService.getUTCZonedDateTime());
        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+model.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.subtitle").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.text").value("Mock text"));
    }

    //</editor-fold>

    //<editor-fold desc="Get item">
    @Test
    public void getBasicItem() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText("Mock full text");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(saveModel);

        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(id);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data.text").value("Mock text"));
    }

    @Test
    public void saveTwoItemsAndGetBothInOrder() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();

        final GlossaryItemDataModel saveModelOne = new GlossaryItemDataModel();
        saveModelOne.setTitle("Mock title 1");
        saveModelOne.setFullText("Mock full text 1");
        saveModelOne.setSubtitle("Mock subtitle 1");
        saveModelOne.setText("Mock text 1");
        saveModelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        final Long idOne = this.saveItem(saveModelOne);
        final SynchronizableGetModel<Long> getModelOne = new SynchronizableGetModel<>();
        getModelOne.setId(idOne);

        final GlossaryItemDataModel saveModelTwo = new GlossaryItemDataModel();
        saveModelTwo.setTitle("Mock title 2");
        saveModelTwo.setFullText("Mock full text 2");
        saveModelTwo.setSubtitle("Mock subtitle 2");
        saveModelTwo.setText("Mock text 2");
        saveModelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        final Long idTwo = this.saveItem(saveModelTwo);
        final SynchronizableGetModel<Long> getModelTwo = new SynchronizableGetModel<>();
        getModelTwo.setId(idTwo);


        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModelOne, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.id").value(idOne.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+saveModelOne.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data.title").value("Mock title 1"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text 1"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle 1"))
                .andExpect(jsonPath("$.data.text").value("Mock text 1"));

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModelTwo, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.id").value(idTwo.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+saveModelTwo.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data.title").value("Mock title 2"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text 2"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle 2"))
                .andExpect(jsonPath("$.data.text").value("Mock text 2"));
    }

    @Test
    public void saveTwoItemsAndGetBothInInverseOrder() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();

        final GlossaryItemDataModel saveModelOne = new GlossaryItemDataModel();
        saveModelOne.setTitle("Mock title 1");
        saveModelOne.setFullText("Mock full text 1");
        saveModelOne.setSubtitle("Mock subtitle 1");
        saveModelOne.setText("Mock text 1");
        saveModelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        final Long idOne = this.saveItem(saveModelOne);
        final SynchronizableGetModel<Long> getModelOne = new SynchronizableGetModel<>();
        getModelOne.setId(idOne);

        final GlossaryItemDataModel saveModelTwo = new GlossaryItemDataModel();
        saveModelTwo.setTitle("Mock title 2");
        saveModelTwo.setFullText("Mock full text 2");
        saveModelTwo.setSubtitle("Mock subtitle 2");
        saveModelTwo.setText("Mock text 2");
        saveModelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        final Long idTwo = this.saveItem(saveModelTwo);
        final SynchronizableGetModel<Long> getModelTwo = new SynchronizableGetModel<>();
        getModelTwo.setId(idTwo);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModelTwo, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.id").value(idTwo.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+saveModelTwo.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data.title").value("Mock title 2"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text 2"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle 2"))
                .andExpect(jsonPath("$.data.text").value("Mock text 2"));

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModelOne, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.id").value(idOne.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+saveModelOne.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data.title").value("Mock title 1"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text 1"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle 1"))
                .andExpect(jsonPath("$.data.text").value("Mock text 1"));
    }

    @Test
    public void tryGetWithInvalidId() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();

        final SynchronizableGetModel<Long> getModelOne = new SynchronizableGetModel<>();
        getModelOne.setId(99999999999999999L);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModelOne, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.error").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data").value(IsNull.nullValue()));
    }

    @Test
    public void tryGetWithoutId() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();

        final SynchronizableGetModel<Long> getModelOne = new SynchronizableGetModel<>();
        getModelOne.setId(null);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModelOne, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getItemWithAttributesInMaxSize() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();

        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("cinquenta é o máximo cinquenta é o máximo 12345677");
        saveModel.setFullText("mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o mámil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má  mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o mámil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má  mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o mámil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má  mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o mámil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má  mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o mámil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má  mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o mámil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má  mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o mámil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má  mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o mámil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má  mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o mámil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má  mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o mámil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má  mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má ");
        saveModel.setSubtitle("vinte é o máximo 123");
        saveModel.setText("mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o mámil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má  mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má ");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());
        final Long id = this.saveItem(saveModel);
        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(id);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+saveModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data.title").value("cinquenta é o máximo cinquenta é o máximo 12345677"))
                .andExpect(jsonPath("$.data.fullText").value("mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o mámil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má  mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o mámil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má  mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o mámil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má  mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o mámil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má  mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o mámil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má  mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o mámil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má  mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o mámil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má  mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o mámil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má  mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o mámil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má  mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o mámil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má  mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má "))
                .andExpect(jsonPath("$.data.subtitle").value("vinte é o máximo 123"))
                .andExpect(jsonPath("$.data.text").value("mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o mámil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má  mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximo mil é o máximomil é o má "));
    }

    @Test
    public void getItemWithAttributesInMixSize() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();

        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("c");
        saveModel.setFullText("f");
        saveModel.setSubtitle("s");
        saveModel.setText("t");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());
        final Long id = this.saveItem(saveModel);
        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(id);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+saveModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data.title").value("c"))
                .andExpect(jsonPath("$.data.fullText").value("f"))
                .andExpect(jsonPath("$.data.subtitle").value("s"))
                .andExpect(jsonPath("$.data.text").value("t"));
    }

    @Test
    public void getItemWithNullFullText() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();

        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("c");
        saveModel.setFullText(null);
        saveModel.setSubtitle("s");
        saveModel.setText("t");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());
        final Long id = this.saveItem(saveModel);
        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(id);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+saveModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data.title").value("c"))
                .andExpect(jsonPath("$.data.fullText").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.subtitle").value("s"))
                .andExpect(jsonPath("$.data.text").value("t"));
    }

    @Test
    public void getItemWithNullSubtitle() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();

        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("c");
        saveModel.setFullText("f");
        saveModel.setSubtitle(null);
        saveModel.setText("t");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());
        final Long id = this.saveItem(saveModel);
        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(id);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+saveModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data.title").value("c"))
                .andExpect(jsonPath("$.data.fullText").value("f"))
                .andExpect(jsonPath("$.data.subtitle").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.text").value("t"));
    }

    @Test
    public void getItemWithAllPossibleNullFields() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();

        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("c");
        saveModel.setFullText(null);
        saveModel.setSubtitle(null);
        saveModel.setText("t");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());
        final Long id = this.saveItem(saveModel);
        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(id);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+saveModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data.title").value("c"))
                .andExpect(jsonPath("$.data.fullText").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.subtitle").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.text").value("t"));
    }
    //</editor-fold>

    //<editor-fold desc="Delete item">
    @Test
    public void deleteBasicItem() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText("Mock full text");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(saveModel);

        final SynchronizableDeleteModel<Long> deleteModel = new SynchronizableDeleteModel<>();
        deleteModel.setId(id);
        deleteModel.setLastUpdate(clockService.getUTCZonedDateTime());

        mvc.perform(delete("/public/glossary/delete/")
                .content(JsonUtils.convertToJson(deleteModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data").value(IsNull.nullValue()));
    }

    @Test
    public void deleteItemWithSameLastUpdate() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText("Mock full text");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(saveModel);

        final SynchronizableDeleteModel<Long> deleteModel = new SynchronizableDeleteModel<>();
        deleteModel.setId(id);
        deleteModel.setLastUpdate(saveModel.getLastUpdate());

        mvc.perform(delete("/public/glossary/delete/")
                .content(JsonUtils.convertToJson(deleteModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data").value(IsNull.nullValue()));
    }

    @Test
    public void tryDeleteBasicItemWithOlderLastUpdate() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText("Mock full text");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(saveModel);

        final SynchronizableDeleteModel<Long> deleteModel = new SynchronizableDeleteModel<>();
        deleteModel.setId(id);
        deleteModel.setLastUpdate(saveModel.getLastUpdate().minusMinutes(1));

        mvc.perform(delete("/public/glossary/delete/")
                .content(JsonUtils.convertToJson(deleteModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.error").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data.text").value("Mock text"))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+saveModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data.id").value(id.toString()));
    }

    @Test
    public void tryDeleteItemWithInvalidId() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final SynchronizableDeleteModel<Long> deleteModel = new SynchronizableDeleteModel<>();
        deleteModel.setId(99999999999999L);
        deleteModel.setLastUpdate(clockService.getUTCZonedDateTime());

        mvc.perform(delete("/public/glossary/delete/")
                .content(JsonUtils.convertToJson(deleteModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.error").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data").value(IsNull.nullValue()));
    }

    @Test
    public void tryDeleteItemWithoutSendingId() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final SynchronizableDeleteModel<Long> deleteModel = new SynchronizableDeleteModel<>();
        deleteModel.setLastUpdate(clockService.getUTCZonedDateTime());

        mvc.perform(delete("/public/glossary/delete/")
                .content(JsonUtils.convertToJson(deleteModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void tryDeleteItemWithoutSendingLastUpdate() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText("Mock full text");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(saveModel);

        final SynchronizableDeleteModel<Long> deleteModel = new SynchronizableDeleteModel<>();
        deleteModel.setId(id);

        mvc.perform(delete("/public/glossary/delete/")
                .content(JsonUtils.convertToJson(deleteModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    //</editor-fold>

    //<editor-fold desc="Update Item">
    @Test
    public void updateBasicItem() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText("Mock full text");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(saveModel);

        final GlossaryItemDataModel updateModel = new GlossaryItemDataModel();
        updateModel.setTitle("Mock title updated");
        updateModel.setFullText("Mock full text update");
        updateModel.setText("Mock text updated");
        updateModel.setSubtitle("Mock subtitle up");
        updateModel.setLastUpdate(clockService.getUTCZonedDateTime());
        updateModel.setId(id);

        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(updateModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title updated"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text update"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle up"))
                .andExpect(jsonPath("$.data.text").value("Mock text updated"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));

        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(id);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title updated"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text update"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle up"))
                .andExpect(jsonPath("$.data.text").value("Mock text updated"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));
    }

    @Test
    public void updateOnlyTitle() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText("Mock full text");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(saveModel);

        final GlossaryItemDataModel updateModel = new GlossaryItemDataModel();
        updateModel.setTitle("Mock title updated");
        updateModel.setFullText(saveModel.getFullText());
        updateModel.setText(saveModel.getText());
        updateModel.setSubtitle(saveModel.getSubtitle());
        updateModel.setLastUpdate(clockService.getUTCZonedDateTime());
        updateModel.setId(id);

        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(updateModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title updated"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data.text").value("Mock text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));

        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(id);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title updated"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data.text").value("Mock text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));
    }

    @Test
    public void updateOnlyFullText() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText("Mock full text");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(saveModel);

        final GlossaryItemDataModel updateModel = new GlossaryItemDataModel();
        updateModel.setTitle(saveModel.getTitle());
        updateModel.setFullText("new full text");
        updateModel.setText(saveModel.getText());
        updateModel.setSubtitle(saveModel.getSubtitle());
        updateModel.setLastUpdate(clockService.getUTCZonedDateTime());
        updateModel.setId(id);

        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(updateModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("new full text"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data.text").value("Mock text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));

        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(id);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("new full text"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data.text").value("Mock text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));
    }

    @Test
    public void updateOnlySubstitle() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText("Mock full text");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(saveModel);

        final GlossaryItemDataModel updateModel = new GlossaryItemDataModel();
        updateModel.setTitle(saveModel.getTitle());
        updateModel.setFullText(saveModel.getFullText());
        updateModel.setText(saveModel.getText());
        updateModel.setSubtitle("new subtitle");
        updateModel.setLastUpdate(clockService.getUTCZonedDateTime());
        updateModel.setId(id);

        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(updateModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data.subtitle").value("new subtitle"))
                .andExpect(jsonPath("$.data.text").value("Mock text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));

        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(id);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data.subtitle").value("new subtitle"))
                .andExpect(jsonPath("$.data.text").value("Mock text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));
    }

    @Test
    public void updateOnlyText() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText("Mock full text");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(saveModel);

        final GlossaryItemDataModel updateModel = new GlossaryItemDataModel();
        updateModel.setTitle(saveModel.getTitle());
        updateModel.setFullText(saveModel.getFullText());
        updateModel.setText("new text");
        updateModel.setSubtitle(saveModel.getSubtitle());
        updateModel.setLastUpdate(clockService.getUTCZonedDateTime());
        updateModel.setId(id);

        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(updateModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data.text").value("new text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));

        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(id);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data.text").value("new text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));
    }

    @Test
    public void updatePossibleItemsToNull() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText("Mock full text");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(saveModel);

        final GlossaryItemDataModel updateModel = new GlossaryItemDataModel();
        updateModel.setTitle(saveModel.getTitle());
        updateModel.setFullText(null);
        updateModel.setText(saveModel.getText());
        updateModel.setSubtitle(null);
        updateModel.setLastUpdate(clockService.getUTCZonedDateTime());
        updateModel.setId(id);

        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(updateModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.subtitle").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.text").value("Mock text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));

        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(id);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.subtitle").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.text").value("Mock text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));
    }

    @Test
    public void updateNullAttributes() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText(null);
        saveModel.setSubtitle(null);
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(saveModel);

        final GlossaryItemDataModel updateModel = new GlossaryItemDataModel();
        updateModel.setTitle(saveModel.getTitle());
        updateModel.setFullText("add value");
        updateModel.setText(saveModel.getText());
        updateModel.setSubtitle("add value");
        updateModel.setLastUpdate(clockService.getUTCZonedDateTime());
        updateModel.setId(id);

        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(updateModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("add value"))
                .andExpect(jsonPath("$.data.subtitle").value("add value"))
                .andExpect(jsonPath("$.data.text").value("Mock text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));

        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(id);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("add value"))
                .andExpect(jsonPath("$.data.subtitle").value("add value"))
                .andExpect(jsonPath("$.data.text").value("Mock text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));
    }

    @Test
    public void tryUpdateWithOlderLastUpdate() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText("Mock full text");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(saveModel);

        final GlossaryItemDataModel updateModel = new GlossaryItemDataModel();
        updateModel.setTitle("new title");
        updateModel.setFullText("new full text");
        updateModel.setText("new text");
        updateModel.setSubtitle("new subtitle");
        updateModel.setLastUpdate(clockService.getUTCZonedDateTime().minusDays(1));
        updateModel.setId(id);

        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(updateModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data.text").value("Mock text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+saveModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));

        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(id);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data.text").value("Mock text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+saveModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));
    }

    @Test
    public void updateWithSameLastUpdate() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText("Mock full text");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(saveModel);

        final GlossaryItemDataModel updateModel = new GlossaryItemDataModel();
        updateModel.setTitle("new title");
        updateModel.setFullText("new full text");
        updateModel.setText("new text");
        updateModel.setSubtitle("new subtitle");
        updateModel.setLastUpdate(saveModel.getLastUpdate());
        updateModel.setId(id);

        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(updateModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("new title"))
                .andExpect(jsonPath("$.data.fullText").value("new full text"))
                .andExpect(jsonPath("$.data.subtitle").value("new subtitle"))
                .andExpect(jsonPath("$.data.text").value("new text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));

        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(id);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("new title"))
                .andExpect(jsonPath("$.data.fullText").value("new full text"))
                .andExpect(jsonPath("$.data.subtitle").value("new subtitle"))
                .andExpect(jsonPath("$.data.text").value("new text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));
    }

    @Test
    public void updateWithOneSecondMoreUpdated() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText("Mock full text");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(saveModel);

        final GlossaryItemDataModel updateModel = new GlossaryItemDataModel();
        updateModel.setTitle("new title");
        updateModel.setFullText("new full text");
        updateModel.setText("new text");
        updateModel.setSubtitle("new subtitle");
        updateModel.setLastUpdate(saveModel.getLastUpdate().plusSeconds(1));
        updateModel.setId(id);

        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(updateModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("new title"))
                .andExpect(jsonPath("$.data.fullText").value("new full text"))
                .andExpect(jsonPath("$.data.subtitle").value("new subtitle"))
                .andExpect(jsonPath("$.data.text").value("new text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));

        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(id);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("new title"))
                .andExpect(jsonPath("$.data.fullText").value("new full text"))
                .andExpect(jsonPath("$.data.subtitle").value("new subtitle"))
                .andExpect(jsonPath("$.data.text").value("new text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));
    }

    @Test
    public void updateWithOneMinuteMoreUpdated() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText("Mock full text");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(saveModel);

        final GlossaryItemDataModel updateModel = new GlossaryItemDataModel();
        updateModel.setTitle("new title");
        updateModel.setFullText("new full text");
        updateModel.setText("new text");
        updateModel.setSubtitle("new subtitle");
        updateModel.setLastUpdate(saveModel.getLastUpdate().plusMinutes(1));
        updateModel.setId(id);

        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(updateModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("new title"))
                .andExpect(jsonPath("$.data.fullText").value("new full text"))
                .andExpect(jsonPath("$.data.subtitle").value("new subtitle"))
                .andExpect(jsonPath("$.data.text").value("new text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));

        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(id);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("new title"))
                .andExpect(jsonPath("$.data.fullText").value("new full text"))
                .andExpect(jsonPath("$.data.subtitle").value("new subtitle"))
                .andExpect(jsonPath("$.data.text").value("new text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));
    }

    @Test
    public void updateWithOneHourMoreUpdated() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText("Mock full text");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(saveModel);

        final GlossaryItemDataModel updateModel = new GlossaryItemDataModel();
        updateModel.setTitle("new title");
        updateModel.setFullText("new full text");
        updateModel.setText("new text");
        updateModel.setSubtitle("new subtitle");
        updateModel.setLastUpdate(saveModel.getLastUpdate().plusHours(1));
        updateModel.setId(id);

        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(updateModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("new title"))
                .andExpect(jsonPath("$.data.fullText").value("new full text"))
                .andExpect(jsonPath("$.data.subtitle").value("new subtitle"))
                .andExpect(jsonPath("$.data.text").value("new text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));

        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(id);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("new title"))
                .andExpect(jsonPath("$.data.fullText").value("new full text"))
                .andExpect(jsonPath("$.data.subtitle").value("new subtitle"))
                .andExpect(jsonPath("$.data.text").value("new text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));
    }

    @Test
    public void updateWithOneDayMoreUpdated() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText("Mock full text");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(saveModel);

        final GlossaryItemDataModel updateModel = new GlossaryItemDataModel();
        updateModel.setTitle("new title");
        updateModel.setFullText("new full text");
        updateModel.setText("new text");
        updateModel.setSubtitle("new subtitle");
        updateModel.setLastUpdate(saveModel.getLastUpdate().plusDays(1));
        updateModel.setId(id);

        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(updateModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("new title"))
                .andExpect(jsonPath("$.data.fullText").value("new full text"))
                .andExpect(jsonPath("$.data.subtitle").value("new subtitle"))
                .andExpect(jsonPath("$.data.text").value("new text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));

        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(id);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("new title"))
                .andExpect(jsonPath("$.data.fullText").value("new full text"))
                .andExpect(jsonPath("$.data.subtitle").value("new subtitle"))
                .andExpect(jsonPath("$.data.text").value("new text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));
    }

    @Test
    public void updateWithOneMonthMoreUpdated() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText("Mock full text");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(saveModel);

        final GlossaryItemDataModel updateModel = new GlossaryItemDataModel();
        updateModel.setTitle("new title");
        updateModel.setFullText("new full text");
        updateModel.setText("new text");
        updateModel.setSubtitle("new subtitle");
        updateModel.setLastUpdate(saveModel.getLastUpdate().plusWeeks(1));
        updateModel.setId(id);

        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(updateModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("new title"))
                .andExpect(jsonPath("$.data.fullText").value("new full text"))
                .andExpect(jsonPath("$.data.subtitle").value("new subtitle"))
                .andExpect(jsonPath("$.data.text").value("new text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));

        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(id);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("new title"))
                .andExpect(jsonPath("$.data.fullText").value("new full text"))
                .andExpect(jsonPath("$.data.subtitle").value("new subtitle"))
                .andExpect(jsonPath("$.data.text").value("new text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));
    }

    @Test
    public void updateWithOneYearMoreUpdated() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText("Mock full text");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(saveModel);

        final GlossaryItemDataModel updateModel = new GlossaryItemDataModel();
        updateModel.setTitle("new title");
        updateModel.setFullText("new full text");
        updateModel.setText("new text");
        updateModel.setSubtitle("new subtitle");
        updateModel.setLastUpdate(saveModel.getLastUpdate().plusYears(1));
        updateModel.setId(id);

        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(updateModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("new title"))
                .andExpect(jsonPath("$.data.fullText").value("new full text"))
                .andExpect(jsonPath("$.data.subtitle").value("new subtitle"))
                .andExpect(jsonPath("$.data.text").value("new text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));

        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(id);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("new title"))
                .andExpect(jsonPath("$.data.fullText").value("new full text"))
                .andExpect(jsonPath("$.data.subtitle").value("new subtitle"))
                .andExpect(jsonPath("$.data.text").value("new text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));
    }
    //</editor-fold>

    //<editor-fold desc="Save with specific Glossary rules">
    @Test
    public void updateItemUsingSameTitleWithoutId() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText("Mock full text");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(saveModel);

        final GlossaryItemDataModel updateModel = new GlossaryItemDataModel();
        updateModel.setTitle("Mock title");
        updateModel.setFullText("Mock full text updated");
        updateModel.setSubtitle("Mock subtitle up");
        updateModel.setText("Mock text updated");
        updateModel.setLastUpdate(clockService.getUTCZonedDateTime());

        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(updateModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text updated"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle up"))
                .andExpect(jsonPath("$.data.text").value("Mock text updated"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));

        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(id);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text updated"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle up"))
                .andExpect(jsonPath("$.data.text").value("Mock text updated"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));
    }

    @Test
    public void updateItemUsingSameTitleWithoutIdAndOneMonthMoreUpdated() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText("Mock full text");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(saveModel);

        final GlossaryItemDataModel updateModel = new GlossaryItemDataModel();
        updateModel.setTitle("Mock title");
        updateModel.setFullText("Mock full text updated");
        updateModel.setSubtitle("Mock subtitle up");
        updateModel.setText("Mock text updated");
        updateModel.setLastUpdate(clockService.getUTCZonedDateTime().plusMonths(1));

        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(updateModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text updated"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle up"))
                .andExpect(jsonPath("$.data.text").value("Mock text updated"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));

        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(id);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text updated"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle up"))
                .andExpect(jsonPath("$.data.text").value("Mock text updated"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));
    }

    @Test
    public void updateItemUsingSameTitleWithoutIdAndNullAttributes() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText("Mock full text");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(saveModel);

        final GlossaryItemDataModel updateModel = new GlossaryItemDataModel();
        updateModel.setTitle("Mock title");
        updateModel.setFullText(null);
        updateModel.setSubtitle(null);
        updateModel.setText("Mock text updated");
        updateModel.setLastUpdate(clockService.getUTCZonedDateTime().plusMonths(1));

        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(updateModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.subtitle").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.text").value("Mock text updated"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));

        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(id);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.subtitle").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.text").value("Mock text updated"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+updateModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));
    }

    @Test
    public void tryUpdateItemUsingSameTitleButOutdated() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText("Mock full text");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(saveModel);

        final GlossaryItemDataModel updateModel = new GlossaryItemDataModel();
        updateModel.setTitle("Mock title");
        updateModel.setFullText("Mock full text updated");
        updateModel.setSubtitle("Mock subtitle up");
        updateModel.setText("Mock text updated");
        updateModel.setLastUpdate(clockService.getUTCZonedDateTime().minusDays(1));

        mvc.perform(post("/public/glossary/save/")
                .content(JsonUtils.convertToJson(updateModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data.text").value("Mock text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+saveModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));

        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(id);

        mvc.perform(get("/public/glossary/get/")
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data.text").value("Mock text"))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+saveModel.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")));
    }
    //</editor-fold>

    //<editor-fold desc="Save all items">
    @Test
    public void saveAllBasicItems() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text 1");
        modelOne.setSubtitle("Mock subtitle 1");
        modelOne.setText("Mock text 1");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 2");
        modelTwo.setFullText("Mock full text 2");
        modelTwo.setSubtitle("Mock subtitle 2");
        modelTwo.setText("Mock text 2");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelTwo);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setTitle("Mock title 3");
        modelThree.setFullText("Mock full text 3");
        modelThree.setSubtitle("Mock subtitle 3");
        modelThree.setText("Mock text 3");
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelThree);

        final SynchronizableSaveAllModel<Long, GlossaryItemDataModel> saveModel = new SynchronizableSaveAllModel<>();
        saveModel.setData(data);

        mvc.perform(post("/public/glossary/save/all/")
                .content(JsonUtils.convertToJson(saveModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 1' && @.text=='Mock text 1' && @.fullText=='Mock full text 1' && @.subtitle=='Mock subtitle 1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 2' && @.text=='Mock text 2' && @.fullText=='Mock full text 2' && @.subtitle=='Mock subtitle 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 3' && @.text=='Mock text 3' && @.fullText=='Mock full text 3' && @.subtitle=='Mock subtitle 3')]").isNotEmpty());
    }

    @Test
    public void saveAllItemsWithNullAttributes() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text 1");
        modelOne.setSubtitle("Mock subtitle 1");
        modelOne.setText("Mock text 1");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 2");
        modelTwo.setFullText(null);
        modelTwo.setSubtitle(null);
        modelTwo.setText("Mock text 2");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelTwo);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setTitle("Mock title 3");
        modelThree.setFullText(null);
        modelThree.setSubtitle(null);
        modelThree.setText("Mock text 3");
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelThree);

        final SynchronizableSaveAllModel<Long, GlossaryItemDataModel> saveModel = new SynchronizableSaveAllModel<>();
        saveModel.setData(data);

        mvc.perform(post("/public/glossary/save/all/")
                .content(JsonUtils.convertToJson(saveModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.length()").value("3"))
                .andExpect(jsonPath(generateExistsPattern(modelOne)).isNotEmpty())
                .andExpect(jsonPath(generateExistsPattern(modelTwo)).isNotEmpty())
                .andExpect(jsonPath(generateExistsPattern(modelThree)).isNotEmpty());
    }

    @Test
    public void trySaveAllItemsWhereOneHasNullTitle() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text");
        modelOne.setSubtitle("Mock subtitle");
        modelOne.setText("Mock text");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setFullText(null);
        modelTwo.setSubtitle(null);
        modelTwo.setText("Mock text");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelTwo);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setTitle("Mock title 3");
        modelThree.setFullText(null);
        modelThree.setSubtitle(null);
        modelThree.setText("Mock text");
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelThree);

        final SynchronizableSaveAllModel<Long, GlossaryItemDataModel> saveModel = new SynchronizableSaveAllModel<>();
        saveModel.setData(data);

        mvc.perform(post("/public/glossary/save/all/")
                .content(JsonUtils.convertToJson(saveModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void trySaveAllItemsWhereOneHasNullText() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text");
        modelOne.setSubtitle("Mock subtitle");
        modelOne.setText("Mock text");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 2");
        modelTwo.setFullText("Mock full text");
        modelTwo.setSubtitle("Mock subtitle");
        modelTwo.setText("Mock text");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelTwo);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setTitle("Mock title 3");
        modelThree.setFullText("Mock full text");
        modelThree.setSubtitle("Mock subtitle");
        modelThree.setText(null);
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelThree);

        final SynchronizableSaveAllModel<Long, GlossaryItemDataModel> saveModel = new SynchronizableSaveAllModel<>();
        saveModel.setData(data);

        mvc.perform(post("/public/glossary/save/all/")
                .content(JsonUtils.convertToJson(saveModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void trySaveAllItemsWhereOneHasNullTextAndOtherWithNullTitle() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text");
        modelOne.setSubtitle("Mock subtitle");
        modelOne.setText("Mock text");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 2");
        modelTwo.setFullText("Mock full text");
        modelTwo.setSubtitle("Mock subtitle");
        modelTwo.setText(null);
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelTwo);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setFullText("Mock full text");
        modelThree.setSubtitle("Mock subtitle");
        modelThree.setText("Mock text");
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelThree);

        final SynchronizableSaveAllModel<Long, GlossaryItemDataModel> saveModel = new SynchronizableSaveAllModel<>();
        saveModel.setData(data);

        mvc.perform(post("/public/glossary/save/all/")
                .content(JsonUtils.convertToJson(saveModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveAllItemsWithAllPossibleNullAttributes() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText(null);
        modelOne.setSubtitle(null);
        modelOne.setText("Mock text 1");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 2");
        modelTwo.setFullText(null);
        modelTwo.setSubtitle(null);
        modelTwo.setText("Mock text 2");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelTwo);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setTitle("Mock title 3");
        modelThree.setFullText(null);
        modelThree.setSubtitle(null);
        modelThree.setText("Mock text 3");
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelThree);

        final SynchronizableSaveAllModel<Long, GlossaryItemDataModel> saveModel = new SynchronizableSaveAllModel<>();
        saveModel.setData(data);

        mvc.perform(post("/public/glossary/save/all/")
                .content(JsonUtils.convertToJson(saveModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.length()").value("3"))
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 1' && @.text=='Mock text 1' && @.fullText==null && @.subtitle==null)]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 2' && @.text=='Mock text 2' && @.fullText==null && @.subtitle==null)]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 3' && @.text=='Mock text 3' && @.fullText==null && @.subtitle==null)]").isNotEmpty());
    }

    @Test
    public void trySaveAllItemsWhereOneHasTitleLengthGreaterThanMax() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text");
        modelOne.setSubtitle("Mock subtitle");
        modelOne.setText("Mock text");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 1Mock title 1Mock title 1Mock title 1Mock title 1");
        modelTwo.setFullText("Mock full text");
        modelTwo.setSubtitle("Mock subtitle");
        modelTwo.setText("Mock text");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelTwo);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setTitle("Mock title 3");
        modelThree.setFullText("Mock full text");
        modelThree.setSubtitle("Mock subtitle");
        modelThree.setText("Mock text");
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelThree);

        final SynchronizableSaveAllModel<Long, GlossaryItemDataModel> saveModel = new SynchronizableSaveAllModel<>();
        saveModel.setData(data);

        mvc.perform(post("/public/glossary/save/all/")
                .content(JsonUtils.convertToJson(saveModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void trySaveAllItemsWhereOneHasTextLengthGreaterThanMax() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text");
        modelOne.setSubtitle("Mock subtitle");
        modelOne.setText("Mock text");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 1");
        modelTwo.setFullText("Mock full text");
        modelTwo.setSubtitle("Mock subtitle");
        modelTwo.setText("Mock text");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelTwo);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setTitle("Mock title 3");
        modelThree.setFullText("Mock full text");
        modelThree.setSubtitle("Mock subtitle");
        modelThree.setText("Mock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock text");
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelThree);

        final SynchronizableSaveAllModel<Long, GlossaryItemDataModel> saveModel = new SynchronizableSaveAllModel<>();
        saveModel.setData(data);

        mvc.perform(post("/public/glossary/save/all/")
                .content(JsonUtils.convertToJson(saveModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void trySaveAllItemsWhereOneHasFullTextLengthGreaterThanMax() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock textMock text");
        modelOne.setSubtitle("Mock subtitle");
        modelOne.setText("Mock text");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 1");
        modelTwo.setFullText("Mock full text");
        modelTwo.setSubtitle("Mock subtitle");
        modelTwo.setText("Mock text");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelTwo);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setTitle("Mock title 3");
        modelThree.setFullText("Mock full text");
        modelThree.setSubtitle("Mock subtitle");
        modelThree.setText("Mock text");
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelThree);

        final SynchronizableSaveAllModel<Long, GlossaryItemDataModel> saveModel = new SynchronizableSaveAllModel<>();
        saveModel.setData(data);

        mvc.perform(post("/public/glossary/save/all/")
                .content(JsonUtils.convertToJson(saveModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void trySaveAllItemsWhereOneHasSubtitleLengthGreaterThanMax() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text");
        modelOne.setSubtitle("Mock subtitle");
        modelOne.setText("Mock text");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 1");
        modelTwo.setFullText("Mock full text");
        modelTwo.setSubtitle("Mock subtitleMock subtitle");
        modelTwo.setText("Mock text");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelTwo);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setTitle("Mock title 3");
        modelThree.setFullText("Mock full text");
        modelThree.setSubtitle("Mock subtitle");
        modelThree.setText("Mock text");
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelThree);

        final SynchronizableSaveAllModel<Long, GlossaryItemDataModel> saveModel = new SynchronizableSaveAllModel<>();
        saveModel.setData(data);

        mvc.perform(post("/public/glossary/save/all/")
                .content(JsonUtils.convertToJson(saveModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void trySaveAllItemsWhereOneHasTitleLengthSmallerThanMin() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text");
        modelOne.setSubtitle("Mock subtitle");
        modelOne.setText("Mock text");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("");
        modelTwo.setFullText("Mock full text");
        modelTwo.setSubtitle("Mock subtitleMock subtitle");
        modelTwo.setText("Mock text");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelTwo);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setTitle("Mock title 3");
        modelThree.setFullText("Mock full text");
        modelThree.setSubtitle("Mock subtitle");
        modelThree.setText("Mock text");
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelThree);

        final SynchronizableSaveAllModel<Long, GlossaryItemDataModel> saveModel = new SynchronizableSaveAllModel<>();
        saveModel.setData(data);

        mvc.perform(post("/public/glossary/save/all/")
                .content(JsonUtils.convertToJson(saveModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void trySaveEmptyList() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final SynchronizableSaveAllModel<Long, GlossaryItemDataModel> saveModel = new SynchronizableSaveAllModel<>();
        saveModel.setData(data);

        mvc.perform(post("/public/glossary/save/all/")
                .content(JsonUtils.convertToJson(saveModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }



    //</editor-fold>

    //<editor-fold desc="Save all items with specific rules">

    @Test
    public void saveTwoItemsAndUpdateOneWithoutIdWithSaveTitle() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text");
        modelOne.setSubtitle("Mock subtitle");
        modelOne.setText("Mock text");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(modelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 1");
        modelTwo.setFullText("Mock full text updated");
        modelTwo.setSubtitle("Mock subtitle");
        modelTwo.setText("Mock text");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        modelTwo.setId(id);
        data.add(modelTwo);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setTitle("Mock title 2");
        modelThree.setFullText("Mock full text");
        modelThree.setSubtitle("Mock subtitle");
        modelThree.setText("Mock text");
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelThree);

        final SynchronizableSaveAllModel<Long, GlossaryItemDataModel> saveModel = new SynchronizableSaveAllModel<>();
        saveModel.setData(data);

        mvc.perform(post("/public/glossary/save/all/")
                .content(JsonUtils.convertToJson(saveModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data[0].lastUpdate", matchesPattern("^"+modelTwo.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data[0].title").value("Mock title 1"))
                .andExpect(jsonPath("$.data[0].fullText").value("Mock full text updated"))
                .andExpect(jsonPath("$.data[0].subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data[0].text").value("Mock text"))
                .andExpect(jsonPath("$.data[0].id").value(id.toString()))
                .andExpect(jsonPath("$.data[1].lastUpdate", matchesPattern("^"+modelThree.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data[1].title").value("Mock title 2"))
                .andExpect(jsonPath("$.data[1].fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data[1].subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data[1].text").value("Mock text"))
                .andExpect(jsonPath("$.data.length()").value("2"));
    }

    @Test
    public void saveTwoItemsAndUpdateOneWithoutIdWithSaveTitleReverseOrder() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text");
        modelOne.setSubtitle("Mock subtitle");
        modelOne.setText("Mock text");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long id = this.saveItem(modelOne);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setTitle("Mock title 2");
        modelThree.setFullText("Mock full text");
        modelThree.setSubtitle("Mock subtitle");
        modelThree.setText("Mock text");
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelThree);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 1");
        modelTwo.setFullText("Mock full text updated");
        modelTwo.setSubtitle("Mock subtitle");
        modelTwo.setText("Mock text");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelTwo);


        final SynchronizableSaveAllModel<Long, GlossaryItemDataModel> saveModel = new SynchronizableSaveAllModel<>();
        saveModel.setData(data);

        mvc.perform(post("/public/glossary/save/all/")
                .content(JsonUtils.convertToJson(saveModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data[0].lastUpdate", matchesPattern("^"+modelTwo.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data[0].title").value("Mock title 1"))
                .andExpect(jsonPath("$.data[0].fullText").value("Mock full text updated"))
                .andExpect(jsonPath("$.data[0].subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data[0].text").value("Mock text"))
                .andExpect(jsonPath("$.data[0].id").value(id.toString()))
                .andExpect(jsonPath("$.data[1].lastUpdate", matchesPattern("^"+modelThree.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data[1].title").value("Mock title 2"))
                .andExpect(jsonPath("$.data[1].fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data[1].subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data[1].text").value("Mock text"))
                .andExpect(jsonPath("$.data.length()").value("2"));
    }

    @Test
    public void saveThreeItemsWithRepeatingTitleTwiceWhereTheSecondIsMoreUpdated() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text");
        modelOne.setSubtitle("Mock subtitle");
        modelOne.setText("Mock text");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 1");
        modelTwo.setFullText("Mock full text updated");
        modelTwo.setSubtitle("Mock subtitle");
        modelTwo.setText("Mock text");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime().plusMinutes(1));
        data.add(modelTwo);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setTitle("Mock title 2");
        modelThree.setFullText("Mock full text");
        modelThree.setSubtitle("Mock subtitle");
        modelThree.setText("Mock text");
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelThree);

        final SynchronizableSaveAllModel<Long, GlossaryItemDataModel> saveModel = new SynchronizableSaveAllModel<>();
        saveModel.setData(data);

        mvc.perform(post("/public/glossary/save/all/")
                .content(JsonUtils.convertToJson(saveModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data[0].lastUpdate", matchesPattern("^"+modelTwo.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data[0].title").value("Mock title 1"))
                .andExpect(jsonPath("$.data[0].fullText").value("Mock full text updated"))
                .andExpect(jsonPath("$.data[0].subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data[0].text").value("Mock text"))
                .andExpect(jsonPath("$.data[1].lastUpdate", matchesPattern("^"+modelThree.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data[1].title").value("Mock title 2"))
                .andExpect(jsonPath("$.data[1].fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data[1].subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data[1].text").value("Mock text"))
                .andExpect(jsonPath("$.data.length()").value("2"));
    }

    @Test
    public void saveThreeItemsWithRepeatingTitleTwiceWhereTheFirstIsMoreUpdated() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text");
        modelOne.setSubtitle("Mock subtitle");
        modelOne.setText("Mock text");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime().plusMinutes(1));
        data.add(modelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 1");
        modelTwo.setFullText("Mock full text updated");
        modelTwo.setSubtitle("Mock subtitle");
        modelTwo.setText("Mock text");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelTwo);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setTitle("Mock title 2");
        modelThree.setFullText("Mock full text");
        modelThree.setSubtitle("Mock subtitle");
        modelThree.setText("Mock text");
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelThree);

        final SynchronizableSaveAllModel<Long, GlossaryItemDataModel> saveModel = new SynchronizableSaveAllModel<>();
        saveModel.setData(data);

        mvc.perform(post("/public/glossary/save/all/")
                .content(JsonUtils.convertToJson(saveModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data[0].lastUpdate", matchesPattern("^"+modelOne.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data[0].title").value("Mock title 1"))
                .andExpect(jsonPath("$.data[0].fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data[0].subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data[0].text").value("Mock text"))
                .andExpect(jsonPath("$.data[1].lastUpdate", matchesPattern("^"+modelThree.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data[1].title").value("Mock title 2"))
                .andExpect(jsonPath("$.data[1].fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data[1].subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data[1].text").value("Mock text"))
                .andExpect(jsonPath("$.data.length()").value("2"));
    }

    @Test
    public void saveThreeItemsWithRepeatingTitleTwiceAndUpdateOneWithoutIdWithSaveTitle() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text");
        modelOne.setSubtitle("Mock subtitle");
        modelOne.setText("Mock text");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelOne);

        final Long id = this.saveItem(modelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 1");
        modelTwo.setFullText("Mock full text updated");
        modelTwo.setSubtitle("Mock subtitle");
        modelTwo.setText("Mock text");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelTwo);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setTitle("Mock title 2");
        modelThree.setFullText("Mock full text");
        modelThree.setSubtitle("Mock subtitle");
        modelThree.setText("Mock text");
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(modelThree);

        final SynchronizableSaveAllModel<Long, GlossaryItemDataModel> saveModel = new SynchronizableSaveAllModel<>();
        saveModel.setData(data);

        mvc.perform(post("/public/glossary/save/all/")
                .content(JsonUtils.convertToJson(saveModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data[0].lastUpdate", matchesPattern("^"+modelTwo.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data[0].title").value("Mock title 1"))
                .andExpect(jsonPath("$.data[0].fullText").value("Mock full text updated"))
                .andExpect(jsonPath("$.data[0].subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data[0].text").value("Mock text"))
                .andExpect(jsonPath("$.data[0].id").value(id.toString()))
                .andExpect(jsonPath("$.data[1].lastUpdate", matchesPattern("^"+modelThree.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data[1].title").value("Mock title 2"))
                .andExpect(jsonPath("$.data[1].fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data[1].subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data[1].text").value("Mock text"))
                .andExpect(jsonPath("$.data.length()").value("2"));
    }

    //</editor-fold>

    //<editor-fold desc="Get all items">
    @Test
    public void getAllWithTwoItems() throws Exception {
        final GlossaryItemDataModel saveModelOne = new GlossaryItemDataModel();
        saveModelOne.setTitle("Mock title 1");
        saveModelOne.setFullText("Mock full text");
        saveModelOne.setSubtitle("Mock subtitle");
        saveModelOne.setText("Mock text");
        saveModelOne.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idOne = this.saveItem(saveModelOne);

        final GlossaryItemDataModel saveModelTwo = new GlossaryItemDataModel();
        saveModelTwo.setTitle("Mock title 2");
        saveModelTwo.setFullText("Mock full text");
        saveModelTwo.setSubtitle("Mock subtitle");
        saveModelTwo.setText("Mock text");
        saveModelTwo.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idTwo = this.saveItem(saveModelTwo);

        mvc.perform(get("/public/glossary/get/all/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data[0].lastUpdate", matchesPattern("^"+saveModelOne.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data[0].title").value("Mock title 1"))
                .andExpect(jsonPath("$.data[0].fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data[0].subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data[0].text").value("Mock text"))
                .andExpect(jsonPath("$.data[0].id").value(idOne.toString()))
                .andExpect(jsonPath("$.data[1].lastUpdate", matchesPattern("^"+saveModelTwo.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data[1].title").value("Mock title 2"))
                .andExpect(jsonPath("$.data[1].fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data[1].subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data[1].text").value("Mock text"))
                .andExpect(jsonPath("$.data[1].id").value(idTwo.toString()))
                .andExpect(jsonPath("$.data.length()").value("2"));
    }

    @Test
    public void getAllWithZeroItems() throws Exception {
        mvc.perform(get("/public/glossary/get/all/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    public void getAllWithFiveItems() throws Exception {
        final GlossaryItemDataModel saveModelOne = new GlossaryItemDataModel();
        saveModelOne.setTitle("Mock title 1");
        saveModelOne.setFullText("Mock full text");
        saveModelOne.setSubtitle("Mock subtitle");
        saveModelOne.setText("Mock text");
        saveModelOne.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idOne = this.saveItem(saveModelOne);

        final GlossaryItemDataModel saveModelTwo = new GlossaryItemDataModel();
        saveModelTwo.setTitle("Mock title 2");
        saveModelTwo.setFullText("Mock full text");
        saveModelTwo.setSubtitle("Mock subtitle");
        saveModelTwo.setText("Mock text");
        saveModelTwo.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idTwo = this.saveItem(saveModelTwo);

        final GlossaryItemDataModel saveModelThree = new GlossaryItemDataModel();
        saveModelThree.setTitle("Mock title 3");
        saveModelThree.setFullText("Mock full text");
        saveModelThree.setSubtitle("Mock subtitle");
        saveModelThree.setText("Mock text");
        saveModelThree.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idThree = this.saveItem(saveModelThree);

        final GlossaryItemDataModel saveModelFour = new GlossaryItemDataModel();
        saveModelFour.setTitle("Mock title 4");
        saveModelFour.setFullText("Mock full text");
        saveModelFour.setSubtitle("Mock subtitle");
        saveModelFour.setText("Mock text");
        saveModelFour.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idFour = this.saveItem(saveModelFour);

        final GlossaryItemDataModel saveModelFive = new GlossaryItemDataModel();
        saveModelFive.setTitle("Mock title 5");
        saveModelFive.setFullText("Mock full text");
        saveModelFive.setSubtitle("Mock subtitle");
        saveModelFive.setText("Mock text");
        saveModelFive.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idFive = this.saveItem(saveModelFive);

        mvc.perform(get("/public/glossary/get/all/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data[0].lastUpdate", matchesPattern("^"+saveModelOne.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data[0].title").value("Mock title 1"))
                .andExpect(jsonPath("$.data[0].fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data[0].subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data[0].text").value("Mock text"))
                .andExpect(jsonPath("$.data[0].id").value(idOne.toString()))
                .andExpect(jsonPath("$.data[1].lastUpdate", matchesPattern("^"+saveModelTwo.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data[1].title").value("Mock title 2"))
                .andExpect(jsonPath("$.data[1].fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data[1].subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data[1].text").value("Mock text"))
                .andExpect(jsonPath("$.data[1].id").value(idTwo.toString()))
                .andExpect(jsonPath("$.data[2].lastUpdate", matchesPattern("^"+saveModelThree.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data[2].title").value("Mock title 3"))
                .andExpect(jsonPath("$.data[2].fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data[2].subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data[2].text").value("Mock text"))
                .andExpect(jsonPath("$.data[2].id").value(idThree.toString()))
                .andExpect(jsonPath("$.data[3].lastUpdate", matchesPattern("^"+saveModelFour.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data[3].title").value("Mock title 4"))
                .andExpect(jsonPath("$.data[3].fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data[3].subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data[3].text").value("Mock text"))
                .andExpect(jsonPath("$.data[3].id").value(idFour.toString()))
                .andExpect(jsonPath("$.data[4].lastUpdate", matchesPattern("^"+saveModelFive.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data[4].title").value("Mock title 5"))
                .andExpect(jsonPath("$.data[4].fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data[4].subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data[4].text").value("Mock text"))
                .andExpect(jsonPath("$.data[4].id").value(idFive.toString()))
                .andExpect(jsonPath("$.data.length()").value("5"));
    }

    //</editor-fold>

    //<editor-fold desc="Delete all items">
    @Test
    public void deleteFiveItemsOfFive() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();

        final List<SynchronizableDeleteModel<Long>> data = new ArrayList<>();

        final GlossaryItemDataModel saveModelOne = new GlossaryItemDataModel();
        saveModelOne.setTitle("Mock title 1");
        saveModelOne.setFullText("Mock full text");
        saveModelOne.setSubtitle("Mock subtitle");
        saveModelOne.setText("Mock text");
        saveModelOne.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idOne = this.saveItem(saveModelOne);
        final SynchronizableDeleteModel<Long> itemOne = new SynchronizableDeleteModel<>();
        itemOne.setLastUpdate(clockService.getUTCZonedDateTime());
        itemOne.setId(idOne);
        data.add(itemOne);

        final GlossaryItemDataModel saveModelTwo = new GlossaryItemDataModel();
        saveModelTwo.setTitle("Mock title 2");
        saveModelTwo.setFullText("Mock full text");
        saveModelTwo.setSubtitle("Mock subtitle");
        saveModelTwo.setText("Mock text");
        saveModelTwo.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idTwo = this.saveItem(saveModelTwo);
        final SynchronizableDeleteModel<Long> itemTwo = new SynchronizableDeleteModel<>();
        itemTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        itemTwo.setId(idTwo);
        data.add(itemTwo);

        final GlossaryItemDataModel saveModelThree = new GlossaryItemDataModel();
        saveModelThree.setTitle("Mock title 3");
        saveModelThree.setFullText("Mock full text");
        saveModelThree.setSubtitle("Mock subtitle");
        saveModelThree.setText("Mock text");
        saveModelThree.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idThree = this.saveItem(saveModelThree);
        final SynchronizableDeleteModel<Long> itemThree = new SynchronizableDeleteModel<>();
        itemThree.setLastUpdate(clockService.getUTCZonedDateTime());
        itemThree.setId(idThree);
        data.add(itemThree);

        final GlossaryItemDataModel saveModelFour = new GlossaryItemDataModel();
        saveModelFour.setTitle("Mock title 4");
        saveModelFour.setFullText("Mock full text");
        saveModelFour.setSubtitle("Mock subtitle");
        saveModelFour.setText("Mock text");
        saveModelFour.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idFour = this.saveItem(saveModelFour);
        final SynchronizableDeleteModel<Long> itemFour = new SynchronizableDeleteModel<>();
        itemFour.setLastUpdate(clockService.getUTCZonedDateTime());
        itemFour.setId(idFour);
        data.add(itemFour);

        final GlossaryItemDataModel saveModelFive = new GlossaryItemDataModel();
        saveModelFive.setTitle("Mock title 5");
        saveModelFive.setFullText("Mock full text");
        saveModelFive.setSubtitle("Mock subtitle");
        saveModelFive.setText("Mock text");
        saveModelFive.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idFive = this.saveItem(saveModelFive);
        final SynchronizableDeleteModel<Long> itemFive = new SynchronizableDeleteModel<>();
        itemFive.setLastUpdate(clockService.getUTCZonedDateTime());
        itemFive.setId(idFive);
        data.add(itemFive);

        final SynchronizableDeleteAllListModel<Long> model = new SynchronizableDeleteAllListModel<>();
        model.setData(data);

        mvc.perform(delete("/public/glossary/delete/all/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.length()").value("5"))
                .andExpect(jsonPath("$.data[0]").value(idOne.toString()))
                .andExpect(jsonPath("$.data[1]").value(idTwo.toString()))
                .andExpect(jsonPath("$.data[2]").value(idThree.toString()))
                .andExpect(jsonPath("$.data[3]").value(idFour.toString()))
                .andExpect(jsonPath("$.data[4]").value(idFive.toString()));
    }

    @Test
    public void deleteFiveItemsOfFiveWithOneOutdated() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();

        final List<SynchronizableDeleteModel<Long>> data = new ArrayList<>();

        final GlossaryItemDataModel saveModelOne = new GlossaryItemDataModel();
        saveModelOne.setTitle("Mock title 1");
        saveModelOne.setFullText("Mock full text");
        saveModelOne.setSubtitle("Mock subtitle");
        saveModelOne.setText("Mock text");
        saveModelOne.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idOne = this.saveItem(saveModelOne);
        final SynchronizableDeleteModel<Long> itemOne = new SynchronizableDeleteModel<>();
        itemOne.setLastUpdate(clockService.getUTCZonedDateTime().minusHours(1));
        itemOne.setId(idOne);
        data.add(itemOne);

        final GlossaryItemDataModel saveModelTwo = new GlossaryItemDataModel();
        saveModelTwo.setTitle("Mock title 2");
        saveModelTwo.setFullText("Mock full text");
        saveModelTwo.setSubtitle("Mock subtitle");
        saveModelTwo.setText("Mock text");
        saveModelTwo.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idTwo = this.saveItem(saveModelTwo);
        final SynchronizableDeleteModel<Long> itemTwo = new SynchronizableDeleteModel<>();
        itemTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        itemTwo.setId(idTwo);
        data.add(itemTwo);

        final GlossaryItemDataModel saveModelThree = new GlossaryItemDataModel();
        saveModelThree.setTitle("Mock title 3");
        saveModelThree.setFullText("Mock full text");
        saveModelThree.setSubtitle("Mock subtitle");
        saveModelThree.setText("Mock text");
        saveModelThree.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idThree = this.saveItem(saveModelThree);
        final SynchronizableDeleteModel<Long> itemThree = new SynchronizableDeleteModel<>();
        itemThree.setLastUpdate(clockService.getUTCZonedDateTime());
        itemThree.setId(idThree);
        data.add(itemThree);

        final GlossaryItemDataModel saveModelFour = new GlossaryItemDataModel();
        saveModelFour.setTitle("Mock title 4");
        saveModelFour.setFullText("Mock full text");
        saveModelFour.setSubtitle("Mock subtitle");
        saveModelFour.setText("Mock text");
        saveModelFour.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idFour = this.saveItem(saveModelFour);
        final SynchronizableDeleteModel<Long> itemFour = new SynchronizableDeleteModel<>();
        itemFour.setLastUpdate(clockService.getUTCZonedDateTime());
        itemFour.setId(idFour);
        data.add(itemFour);

        final GlossaryItemDataModel saveModelFive = new GlossaryItemDataModel();
        saveModelFive.setTitle("Mock title 5");
        saveModelFive.setFullText("Mock full text");
        saveModelFive.setSubtitle("Mock subtitle");
        saveModelFive.setText("Mock text");
        saveModelFive.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idFive = this.saveItem(saveModelFive);
        final SynchronizableDeleteModel<Long> itemFive = new SynchronizableDeleteModel<>();
        itemFive.setLastUpdate(clockService.getUTCZonedDateTime());
        itemFive.setId(idFive);
        data.add(itemFive);

        final SynchronizableDeleteAllListModel<Long> model = new SynchronizableDeleteAllListModel<>();
        model.setData(data);

        mvc.perform(delete("/public/glossary/delete/all/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.length()").value("4"))
                .andExpect(jsonPath("$.data[0]").value(idTwo.toString()))
                .andExpect(jsonPath("$.data[1]").value(idThree.toString()))
                .andExpect(jsonPath("$.data[2]").value(idFour.toString()))
                .andExpect(jsonPath("$.data[3]").value(idFive.toString()));
    }

    @Test
    public void deleteFiveItemsOfFiveWithTwoOutdated() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();

        final List<SynchronizableDeleteModel<Long>> data = new ArrayList<>();

        final GlossaryItemDataModel saveModelOne = new GlossaryItemDataModel();
        saveModelOne.setTitle("Mock title 1");
        saveModelOne.setFullText("Mock full text");
        saveModelOne.setSubtitle("Mock subtitle");
        saveModelOne.setText("Mock text");
        saveModelOne.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idOne = this.saveItem(saveModelOne);
        final SynchronizableDeleteModel<Long> itemOne = new SynchronizableDeleteModel<>();
        itemOne.setLastUpdate(clockService.getUTCZonedDateTime().minusHours(1));
        itemOne.setId(idOne);
        data.add(itemOne);

        final GlossaryItemDataModel saveModelTwo = new GlossaryItemDataModel();
        saveModelTwo.setTitle("Mock title 2");
        saveModelTwo.setFullText("Mock full text");
        saveModelTwo.setSubtitle("Mock subtitle");
        saveModelTwo.setText("Mock text");
        saveModelTwo.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idTwo = this.saveItem(saveModelTwo);
        final SynchronizableDeleteModel<Long> itemTwo = new SynchronizableDeleteModel<>();
        itemTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        itemTwo.setId(idTwo);
        data.add(itemTwo);

        final GlossaryItemDataModel saveModelThree = new GlossaryItemDataModel();
        saveModelThree.setTitle("Mock title 3");
        saveModelThree.setFullText("Mock full text");
        saveModelThree.setSubtitle("Mock subtitle");
        saveModelThree.setText("Mock text");
        saveModelThree.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idThree = this.saveItem(saveModelThree);
        final SynchronizableDeleteModel<Long> itemThree = new SynchronizableDeleteModel<>();
        itemThree.setLastUpdate(clockService.getUTCZonedDateTime());
        itemThree.setId(idThree);
        data.add(itemThree);

        final GlossaryItemDataModel saveModelFour = new GlossaryItemDataModel();
        saveModelFour.setTitle("Mock title 4");
        saveModelFour.setFullText("Mock full text");
        saveModelFour.setSubtitle("Mock subtitle");
        saveModelFour.setText("Mock text");
        saveModelFour.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idFour = this.saveItem(saveModelFour);
        final SynchronizableDeleteModel<Long> itemFour = new SynchronizableDeleteModel<>();
        itemFour.setLastUpdate(clockService.getUTCZonedDateTime().minusDays(1));
        itemFour.setId(idFour);
        data.add(itemFour);

        final GlossaryItemDataModel saveModelFive = new GlossaryItemDataModel();
        saveModelFive.setTitle("Mock title 5");
        saveModelFive.setFullText("Mock full text");
        saveModelFive.setSubtitle("Mock subtitle");
        saveModelFive.setText("Mock text");
        saveModelFive.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idFive = this.saveItem(saveModelFive);
        final SynchronizableDeleteModel<Long> itemFive = new SynchronizableDeleteModel<>();
        itemFive.setLastUpdate(clockService.getUTCZonedDateTime());
        itemFive.setId(idFive);
        data.add(itemFive);

        final SynchronizableDeleteAllListModel<Long> model = new SynchronizableDeleteAllListModel<>();
        model.setData(data);

        mvc.perform(delete("/public/glossary/delete/all/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.length()").value("3"))
                .andExpect(jsonPath("$.data[0]").value(idTwo.toString()))
                .andExpect(jsonPath("$.data[1]").value(idThree.toString()))
                .andExpect(jsonPath("$.data[2]").value(idFive.toString()));
    }

    @Test
    public void tryDeleteTwoItemsWhereOneIsWithoutLastupdate() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();

        final List<SynchronizableDeleteModel<Long>> data = new ArrayList<>();

        final GlossaryItemDataModel saveModelOne = new GlossaryItemDataModel();
        saveModelOne.setTitle("Mock title 1");
        saveModelOne.setFullText("Mock full text");
        saveModelOne.setSubtitle("Mock subtitle");
        saveModelOne.setText("Mock text");
        saveModelOne.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idOne = this.saveItem(saveModelOne);
        final SynchronizableDeleteModel<Long> itemOne = new SynchronizableDeleteModel<>();
        itemOne.setLastUpdate(clockService.getUTCZonedDateTime());
        itemOne.setId(idOne);
        data.add(itemOne);

        final GlossaryItemDataModel saveModelTwo = new GlossaryItemDataModel();
        saveModelTwo.setTitle("Mock title 2");
        saveModelTwo.setFullText("Mock full text");
        saveModelTwo.setSubtitle("Mock subtitle");
        saveModelTwo.setText("Mock text");
        saveModelTwo.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idTwo = this.saveItem(saveModelTwo);
        final SynchronizableDeleteModel<Long> itemTwo = new SynchronizableDeleteModel<>();
        itemTwo.setId(idTwo);
        data.add(itemTwo);

        final SynchronizableDeleteAllListModel<Long> model = new SynchronizableDeleteAllListModel<>();
        model.setData(data);

        mvc.perform(delete("/public/glossary/delete/all/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void tryDeleteTwoItemsWhereOneIsWithoutId() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();

        final List<SynchronizableDeleteModel<Long>> data = new ArrayList<>();

        final GlossaryItemDataModel saveModelOne = new GlossaryItemDataModel();
        saveModelOne.setTitle("Mock title 1");
        saveModelOne.setFullText("Mock full text");
        saveModelOne.setSubtitle("Mock subtitle");
        saveModelOne.setText("Mock text");
        saveModelOne.setLastUpdate(clockService.getUTCZonedDateTime());

        this.saveItem(saveModelOne);
        final SynchronizableDeleteModel<Long> itemOne = new SynchronizableDeleteModel<>();
        itemOne.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(itemOne);

        final GlossaryItemDataModel saveModelTwo = new GlossaryItemDataModel();
        saveModelTwo.setTitle("Mock title 2");
        saveModelTwo.setFullText("Mock full text");
        saveModelTwo.setSubtitle("Mock subtitle");
        saveModelTwo.setText("Mock text");
        saveModelTwo.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idTwo = this.saveItem(saveModelTwo);
        final SynchronizableDeleteModel<Long> itemTwo = new SynchronizableDeleteModel<>();
        itemTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        itemTwo.setId(idTwo);
        data.add(itemTwo);

        final SynchronizableDeleteAllListModel<Long> model = new SynchronizableDeleteAllListModel<>();
        model.setData(data);

        mvc.perform(delete("/public/glossary/delete/all/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void tryDeleteTwoItemsWithoutId() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();

        final List<SynchronizableDeleteModel<Long>> data = new ArrayList<>();

        final GlossaryItemDataModel saveModelOne = new GlossaryItemDataModel();
        saveModelOne.setTitle("Mock title 1");
        saveModelOne.setFullText("Mock full text");
        saveModelOne.setSubtitle("Mock subtitle");
        saveModelOne.setText("Mock text");
        saveModelOne.setLastUpdate(clockService.getUTCZonedDateTime());

        this.saveItem(saveModelOne);
        final SynchronizableDeleteModel<Long> itemOne = new SynchronizableDeleteModel<>();
        itemOne.setLastUpdate(clockService.getUTCZonedDateTime());

        final GlossaryItemDataModel saveModelTwo = new GlossaryItemDataModel();
        saveModelTwo.setTitle("Mock title 2");
        saveModelTwo.setFullText("Mock full text");
        saveModelTwo.setSubtitle("Mock subtitle");
        saveModelTwo.setText("Mock text");
        saveModelTwo.setLastUpdate(clockService.getUTCZonedDateTime());

        this.saveItem(saveModelTwo);
        final SynchronizableDeleteModel<Long> itemTwo = new SynchronizableDeleteModel<>();
        itemTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        data.add(itemTwo);

        final SynchronizableDeleteAllListModel<Long> model = new SynchronizableDeleteAllListModel<>();
        model.setData(data);

        mvc.perform(delete("/public/glossary/delete/all/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteWithInvalidId() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();

        final List<SynchronizableDeleteModel<Long>> data = new ArrayList<>();

        final SynchronizableDeleteModel<Long> itemOne = new SynchronizableDeleteModel<>();
        itemOne.setLastUpdate(clockService.getUTCZonedDateTime());
        itemOne.setId(99999999999999L);
        data.add(itemOne);

        final SynchronizableDeleteAllListModel<Long> model = new SynchronizableDeleteAllListModel<>();
        model.setData(data);

        mvc.perform(delete("/public/glossary/delete/all/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.length()").value("0"));
    }

    @Test
    public void deleteFiveItemsWhereTwoHasInvalidId() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();

        final List<SynchronizableDeleteModel<Long>> data = new ArrayList<>();

        final GlossaryItemDataModel saveModelOne = new GlossaryItemDataModel();
        saveModelOne.setTitle("Mock title 1");
        saveModelOne.setFullText("Mock full text");
        saveModelOne.setSubtitle("Mock subtitle");
        saveModelOne.setText("Mock text");
        saveModelOne.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idOne = this.saveItem(saveModelOne);
        final SynchronizableDeleteModel<Long> itemOne = new SynchronizableDeleteModel<>();
        itemOne.setLastUpdate(clockService.getUTCZonedDateTime());
        itemOne.setId(idOne);
        data.add(itemOne);

        final GlossaryItemDataModel saveModelTwo = new GlossaryItemDataModel();
        saveModelTwo.setTitle("Mock title 2");
        saveModelTwo.setFullText("Mock full text");
        saveModelTwo.setSubtitle("Mock subtitle");
        saveModelTwo.setText("Mock text");
        saveModelTwo.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idTwo = this.saveItem(saveModelTwo);
        final SynchronizableDeleteModel<Long> itemTwo = new SynchronizableDeleteModel<>();
        itemTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        itemTwo.setId(idTwo);
        data.add(itemTwo);

        final GlossaryItemDataModel saveModelThree = new GlossaryItemDataModel();
        saveModelThree.setTitle("Mock title 3");
        saveModelThree.setFullText("Mock full text");
        saveModelThree.setSubtitle("Mock subtitle");
        saveModelThree.setText("Mock text");
        saveModelThree.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idThree = this.saveItem(saveModelThree);
        final SynchronizableDeleteModel<Long> itemThree = new SynchronizableDeleteModel<>();
        itemThree.setLastUpdate(clockService.getUTCZonedDateTime());
        itemThree.setId(idThree);
        data.add(itemThree);

        final SynchronizableDeleteModel<Long> itemFour = new SynchronizableDeleteModel<>();
        itemFour.setLastUpdate(clockService.getUTCZonedDateTime());
        itemFour.setId(9999999999999L);
        data.add(itemFour);

        final SynchronizableDeleteModel<Long> itemFive = new SynchronizableDeleteModel<>();
        itemFive.setLastUpdate(clockService.getUTCZonedDateTime());
        itemFive.setId(9999999999998L);
        data.add(itemFive);

        final SynchronizableDeleteAllListModel<Long> model = new SynchronizableDeleteAllListModel<>();
        model.setData(data);

        mvc.perform(delete("/public/glossary/delete/all/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.length()").value("3"))
                .andExpect(jsonPath("$.data[0]").value(idOne.toString()))
                .andExpect(jsonPath("$.data[1]").value(idTwo.toString()))
                .andExpect(jsonPath("$.data[2]").value(idThree.toString()));
    }

    @Test
    public void deleteFiveItemsWhereTwoHasTheSameID() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();

        final List<SynchronizableDeleteModel<Long>> data = new ArrayList<>();

        final GlossaryItemDataModel saveModelOne = new GlossaryItemDataModel();
        saveModelOne.setTitle("Mock title 1");
        saveModelOne.setFullText("Mock full text");
        saveModelOne.setSubtitle("Mock subtitle");
        saveModelOne.setText("Mock text");
        saveModelOne.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idOne = this.saveItem(saveModelOne);
        final SynchronizableDeleteModel<Long> itemOne = new SynchronizableDeleteModel<>();
        itemOne.setLastUpdate(clockService.getUTCZonedDateTime());
        itemOne.setId(idOne);
        data.add(itemOne);

        final SynchronizableDeleteModel<Long> itemTwo = new SynchronizableDeleteModel<>();
        itemTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        itemTwo.setId(idOne);
        data.add(itemTwo);

        final GlossaryItemDataModel saveModelThree = new GlossaryItemDataModel();
        saveModelThree.setTitle("Mock title 3");
        saveModelThree.setFullText("Mock full text");
        saveModelThree.setSubtitle("Mock subtitle");
        saveModelThree.setText("Mock text");
        saveModelThree.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idThree = this.saveItem(saveModelThree);
        final SynchronizableDeleteModel<Long> itemThree = new SynchronizableDeleteModel<>();
        itemThree.setLastUpdate(clockService.getUTCZonedDateTime());
        itemThree.setId(idThree);
        data.add(itemThree);

        final GlossaryItemDataModel saveModelFour = new GlossaryItemDataModel();
        saveModelFour.setTitle("Mock title 4");
        saveModelFour.setFullText("Mock full text");
        saveModelFour.setSubtitle("Mock subtitle");
        saveModelFour.setText("Mock text");
        saveModelFour.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idFour = this.saveItem(saveModelFour);
        final SynchronizableDeleteModel<Long> itemFour = new SynchronizableDeleteModel<>();
        itemFour.setLastUpdate(clockService.getUTCZonedDateTime());
        itemFour.setId(idFour);
        data.add(itemFour);

        final GlossaryItemDataModel saveModelFive = new GlossaryItemDataModel();
        saveModelFive.setTitle("Mock title 5");
        saveModelFive.setFullText("Mock full text");
        saveModelFive.setSubtitle("Mock subtitle");
        saveModelFive.setText("Mock text");
        saveModelFive.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idFive = this.saveItem(saveModelFive);
        final SynchronizableDeleteModel<Long> itemFive = new SynchronizableDeleteModel<>();
        itemFive.setLastUpdate(clockService.getUTCZonedDateTime());
        itemFive.setId(idFive);
        data.add(itemFive);

        final SynchronizableDeleteAllListModel<Long> model = new SynchronizableDeleteAllListModel<>();
        model.setData(data);

        mvc.perform(delete("/public/glossary/delete/all/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.length()").value("4"))
                .andExpect(jsonPath("$.data[0]").value(idOne.toString()))
                .andExpect(jsonPath("$.data[1]").value(idThree.toString()))
                .andExpect(jsonPath("$.data[2]").value(idFour.toString()))
                .andExpect(jsonPath("$.data[3]").value(idFive.toString()));
    }

    @Test
    public void deleteFiveItemsWhereThreeHasTheSameIDAndAnotherOneIsOutdated() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();

        final List<SynchronizableDeleteModel<Long>> data = new ArrayList<>();

        final GlossaryItemDataModel saveModelOne = new GlossaryItemDataModel();
        saveModelOne.setTitle("Mock title 1");
        saveModelOne.setFullText("Mock full text");
        saveModelOne.setSubtitle("Mock subtitle");
        saveModelOne.setText("Mock text");
        saveModelOne.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idOne = this.saveItem(saveModelOne);
        final SynchronizableDeleteModel<Long> itemOne = new SynchronizableDeleteModel<>();
        itemOne.setLastUpdate(clockService.getUTCZonedDateTime());
        itemOne.setId(idOne);
        data.add(itemOne);

        final SynchronizableDeleteModel<Long> itemTwo = new SynchronizableDeleteModel<>();
        itemTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        itemTwo.setId(idOne);
        data.add(itemTwo);

        final SynchronizableDeleteModel<Long> itemThree = new SynchronizableDeleteModel<>();
        itemThree.setLastUpdate(clockService.getUTCZonedDateTime());
        itemThree.setId(idOne);
        data.add(itemThree);

        final GlossaryItemDataModel saveModelFour = new GlossaryItemDataModel();
        saveModelFour.setTitle("Mock title 4");
        saveModelFour.setFullText("Mock full text");
        saveModelFour.setSubtitle("Mock subtitle");
        saveModelFour.setText("Mock text");
        saveModelFour.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idFour = this.saveItem(saveModelFour);
        final SynchronizableDeleteModel<Long> itemFour = new SynchronizableDeleteModel<>();
        itemFour.setLastUpdate(clockService.getUTCZonedDateTime().minusDays(1));
        itemFour.setId(idFour);
        data.add(itemFour);

        final GlossaryItemDataModel saveModelFive = new GlossaryItemDataModel();
        saveModelFive.setTitle("Mock title 5");
        saveModelFive.setFullText("Mock full text");
        saveModelFive.setSubtitle("Mock subtitle");
        saveModelFive.setText("Mock text");
        saveModelFive.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idFive = this.saveItem(saveModelFive);
        final SynchronizableDeleteModel<Long> itemFive = new SynchronizableDeleteModel<>();
        itemFive.setLastUpdate(clockService.getUTCZonedDateTime());
        itemFive.setId(idFive);
        data.add(itemFive);

        final SynchronizableDeleteAllListModel<Long> model = new SynchronizableDeleteAllListModel<>();
        model.setData(data);

        mvc.perform(delete("/public/glossary/delete/all/")
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.length()").value("2"))
                .andExpect(jsonPath("$.data[0]").value(idOne.toString()))
                .andExpect(jsonPath("$.data[1]").value(idFive.toString()));
    }
    //</editor-fold>

    //<editor-fold desc="Sync">
    @Test
    public void syncJustNewItems() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text 1");
        modelOne.setSubtitle("Mock subtitle 1");
        modelOne.setText("Mock text 1");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        modelOne.setLocalId(1);
        data.add(modelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 2");
        modelTwo.setFullText("Mock full text 2");
        modelTwo.setSubtitle("Mock subtitle 2");
        modelTwo.setText("Mock text 2");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        modelTwo.setLocalId(2);
        data.add(modelTwo);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setTitle("Mock title 3");
        modelThree.setFullText("Mock full text 3");
        modelThree.setSubtitle("Mock subtitle 3");
        modelThree.setText("Mock text 3");
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        modelThree.setLocalId(3);
        data.add(modelThree);

        final SynchronizableSaveSyncModel<Long, GlossaryItemDataModel> syncModel = new SynchronizableSaveSyncModel<>();
        syncModel.setSave(data);

        mvc.perform(put("/public/glossary/sync_save/")
                .content(JsonUtils.convertToJson(syncModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.length()").value("3"))
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.fullText=='Mock full text 1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.subtitle=='Mock subtitle 1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.text=='Mock text 1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.localId=='1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.fullText=='Mock full text 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.subtitle=='Mock subtitle 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.text=='Mock text 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.localId=='2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 3')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.fullText=='Mock full text 3')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.subtitle=='Mock subtitle 3')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.text=='Mock text 3')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.localId=='3')]").isNotEmpty());
    }

    @Test
    public void syncTwoNewItemsAndUpdateAnother() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text 1");
        modelOne.setSubtitle("Mock subtitle 1");
        modelOne.setText("Mock text 1");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        modelOne.setLocalId(1);

        final Long idOne = this.saveItem(modelOne);

        final GlossaryItemDataModel newModelOne = new GlossaryItemDataModel();
        newModelOne.setTitle("Mock title 1 up");
        newModelOne.setFullText("Mock full text 1 up");
        newModelOne.setSubtitle("Mock subtitle 1 up");
        newModelOne.setText("Mock text 1 up");
        newModelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        newModelOne.setLocalId(1);
        newModelOne.setId(idOne);
        data.add(newModelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 2");
        modelTwo.setFullText("Mock full text 2");
        modelTwo.setSubtitle("Mock subtitle 2");
        modelTwo.setText("Mock text 2");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        modelTwo.setLocalId(2);
        data.add(modelTwo);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setTitle("Mock title 3");
        modelThree.setFullText("Mock full text 3");
        modelThree.setSubtitle("Mock subtitle 3");
        modelThree.setText("Mock text 3");
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        modelThree.setLocalId(3);
        data.add(modelThree);

        final SynchronizableSaveSyncModel<Long, GlossaryItemDataModel> syncModel = new SynchronizableSaveSyncModel<>();
        syncModel.setSave(data);

        mvc.perform(put("/public/glossary/sync_save/")
                .content(JsonUtils.convertToJson(syncModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.length()").value("3"))
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 1 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.fullText=='Mock full text 1 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.subtitle=='Mock subtitle 1 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.text=='Mock text 1 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.localId=='1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.fullText=='Mock full text 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.subtitle=='Mock subtitle 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.text=='Mock text 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.localId=='2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 3')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.fullText=='Mock full text 3')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.subtitle=='Mock subtitle 3')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.text=='Mock text 3')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.localId=='3')]").isNotEmpty());
    }

    @Test
    public void syncOneNewItemsAndUpdateAnothers() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text 1");
        modelOne.setSubtitle("Mock subtitle 1");
        modelOne.setText("Mock text 1");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        modelOne.setLocalId(1);

        final Long idOne = this.saveItem(modelOne);

        final GlossaryItemDataModel newModelOne = new GlossaryItemDataModel();
        newModelOne.setTitle("Mock title 1 up");
        newModelOne.setFullText("Mock full text 1 up");
        newModelOne.setSubtitle("Mock subtitle 1 up");
        newModelOne.setText("Mock text 1 up");
        newModelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        newModelOne.setLocalId(1);
        newModelOne.setId(idOne);
        data.add(newModelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 2");
        modelTwo.setFullText("Mock full text 2");
        modelTwo.setSubtitle("Mock subtitle 2");
        modelTwo.setText("Mock text 2");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        modelTwo.setLocalId(2);

        final Long idTwo = this.saveItem(modelTwo);

        final GlossaryItemDataModel newModelTwo = new GlossaryItemDataModel();
        newModelTwo.setTitle("Mock title 2 up");
        newModelTwo.setFullText("Mock full text 2 up");
        newModelTwo.setSubtitle("Mock subtitle 2 up");
        newModelTwo.setText("Mock text 2 up");
        newModelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        newModelTwo.setLocalId(2);
        newModelTwo.setId(idTwo);
        data.add(newModelTwo);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setTitle("Mock title 3");
        modelThree.setFullText("Mock full text 3");
        modelThree.setSubtitle("Mock subtitle 3");
        modelThree.setText("Mock text 3");
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        modelThree.setLocalId(3);
        data.add(modelThree);

        final SynchronizableSaveSyncModel<Long, GlossaryItemDataModel> syncModel = new SynchronizableSaveSyncModel<>();
        syncModel.setSave(data);

        mvc.perform(put("/public/glossary/sync_save/")
                .content(JsonUtils.convertToJson(syncModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.length()").value("3"))
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 1 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.fullText=='Mock full text 1 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.subtitle=='Mock subtitle 1 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.text=='Mock text 1 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.localId=='1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 2 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.fullText=='Mock full text 2 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.subtitle=='Mock subtitle 2 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.text=='Mock text 2 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.localId=='2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 3')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.fullText=='Mock full text 3')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.subtitle=='Mock subtitle 3')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.text=='Mock text 3')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.localId=='3')]").isNotEmpty());
    }

    @Test
    public void trySyncOneNewItemsAndUpdateAnotherTwoWhereOneHasInvalidAttributes() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text 1");
        modelOne.setSubtitle("Mock subtitle 1");
        modelOne.setText("Mock text 1");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        modelOne.setLocalId(1);

        final Long idOne = this.saveItem(modelOne);

        final GlossaryItemDataModel newModelOne = new GlossaryItemDataModel();
        newModelOne.setTitle("Mock title 1 up");
        newModelOne.setFullText("Mock full text 1 up");
        newModelOne.setSubtitle("Mock subtitle 1 up");
        newModelOne.setText("Mock text 1 up");
        newModelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        newModelOne.setLocalId(1);
        newModelOne.setId(idOne);
        data.add(newModelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 2");
        modelTwo.setFullText("Mock full text 2");
        modelTwo.setSubtitle("Mock subtitle 2");
        modelTwo.setText("Mock text 2");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        modelTwo.setLocalId(2);

        final Long idTwo = this.saveItem(modelTwo);

        final GlossaryItemDataModel newModelTwo = new GlossaryItemDataModel();
        newModelTwo.setTitle("Mock title 2 up");
        newModelTwo.setFullText("Mock full text 2 up");
        newModelTwo.setSubtitle("Mock subtitle 2 up");
        newModelTwo.setText(null);
        newModelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        newModelTwo.setLocalId(2);
        newModelOne.setId(idTwo);
        data.add(newModelTwo);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setTitle("Mock title 3");
        modelThree.setFullText("Mock full text 3");
        modelThree.setSubtitle("Mock subtitle 3");
        modelThree.setText("Mock text 3");
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        modelThree.setLocalId(3);
        data.add(modelThree);

        final SynchronizableSaveSyncModel<Long, GlossaryItemDataModel> syncModel = new SynchronizableSaveSyncModel<>();
        syncModel.setSave(data);

        mvc.perform(put("/public/glossary/sync_save/")
                .content(JsonUtils.convertToJson(syncModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void trySyncOneNewItemsWithInvalidNullAttributeAndUpdateAnotherTwo() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text 1");
        modelOne.setSubtitle("Mock subtitle 1");
        modelOne.setText("Mock text 1");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        modelOne.setLocalId(1);

        final Long idOne = this.saveItem(modelOne);

        final GlossaryItemDataModel newModelOne = new GlossaryItemDataModel();
        newModelOne.setTitle("Mock title 1 up");
        newModelOne.setFullText("Mock full text 1 up");
        newModelOne.setSubtitle("Mock subtitle 1 up");
        newModelOne.setText("Mock text 1 up");
        newModelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        newModelOne.setLocalId(1);
        newModelOne.setId(idOne);
        data.add(newModelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 2");
        modelTwo.setFullText("Mock full text 2");
        modelTwo.setSubtitle("Mock subtitle 2");
        modelTwo.setText("Mock text 2");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        modelTwo.setLocalId(2);

        final Long idTwo = this.saveItem(modelTwo);

        final GlossaryItemDataModel newModelTwo = new GlossaryItemDataModel();
        newModelTwo.setTitle("Mock title 2 up");
        newModelTwo.setFullText("Mock full text 2 up");
        newModelTwo.setSubtitle("Mock subtitle 2 up");
        newModelTwo.setText("Mock text 2 up");
        newModelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        newModelTwo.setLocalId(2);
        newModelOne.setId(idTwo);
        data.add(newModelTwo);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setTitle("Mock title 3");
        modelThree.setFullText("Mock full text 3");
        modelThree.setSubtitle("Mock subtitle 3");
        modelThree.setText(null);
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        modelThree.setLocalId(3);
        data.add(modelThree);

        final SynchronizableSaveSyncModel<Long, GlossaryItemDataModel> syncModel = new SynchronizableSaveSyncModel<>();
        syncModel.setSave(data);

        mvc.perform(put("/public/glossary/sync_save/")
                .content(JsonUtils.convertToJson(syncModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void trySyncOneNewItemsWithInvalidNullAttributeAndUpdateAnotherTwoWhereOneHasInvalidAttributes() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text 1");
        modelOne.setSubtitle("Mock subtitle 1");
        modelOne.setText("Mock text 1");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        modelOne.setLocalId(1);

        final Long idOne = this.saveItem(modelOne);

        final GlossaryItemDataModel newModelOne = new GlossaryItemDataModel();
        newModelOne.setTitle("Mock title 1 up");
        newModelOne.setFullText("Mock full text 1 up");
        newModelOne.setSubtitle("Mock subtitle 1 up");
        newModelOne.setText("Mock text 1 up");
        newModelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        newModelOne.setLocalId(1);
        newModelOne.setId(idOne);
        data.add(newModelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 2");
        modelTwo.setFullText("Mock full text 2");
        modelTwo.setSubtitle("Mock subtitle 2");
        modelTwo.setText("Mock text 2 up");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        modelTwo.setLocalId(2);

        final Long idTwo = this.saveItem(modelTwo);

        final GlossaryItemDataModel newModelTwo = new GlossaryItemDataModel();
        newModelTwo.setTitle("Mock title 2 up");
        newModelTwo.setFullText("Mock full text 2 up");
        newModelTwo.setSubtitle("Mock subtitle 2 up");
        newModelTwo.setText(null);
        newModelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        newModelTwo.setLocalId(2);
        newModelOne.setId(idTwo);
        data.add(newModelTwo);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setTitle("Mock title 3");
        modelThree.setFullText("Mock full text 3");
        modelThree.setSubtitle("Mock subtitle 3");
        modelThree.setText(null);
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        modelThree.setLocalId(3);
        data.add(modelThree);

        final SynchronizableSaveSyncModel<Long, GlossaryItemDataModel> syncModel = new SynchronizableSaveSyncModel<>();
        syncModel.setSave(data);

        mvc.perform(put("/public/glossary/sync_save/")
                .content(JsonUtils.convertToJson(syncModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void syncFistUpdateSecondUpdatedOutdatedThirdNew() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text 1");
        modelOne.setSubtitle("Mock subtitle 1");
        modelOne.setText("Mock text 1");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        modelOne.setLocalId(1);

        final Long idOne = this.saveItem(modelOne);

        final GlossaryItemDataModel newModelOne = new GlossaryItemDataModel();
        newModelOne.setTitle("Mock title 1 up");
        newModelOne.setFullText("Mock full text 1 up");
        newModelOne.setSubtitle("Mock subtitle 1 up");
        newModelOne.setText("Mock text 1 up");
        newModelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        newModelOne.setLocalId(1);
        newModelOne.setId(idOne);
        data.add(newModelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 2");
        modelTwo.setFullText("Mock full text 2");
        modelTwo.setSubtitle("Mock subtitle 2");
        modelTwo.setText("Mock text 2");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        modelTwo.setLocalId(2);

        final Long idTwo = this.saveItem(modelTwo);

        final GlossaryItemDataModel newModelTwo = new GlossaryItemDataModel();
        newModelTwo.setTitle("Mock title 2 up");
        newModelTwo.setFullText("Mock full text 2 up");
        newModelTwo.setSubtitle("Mock subtitle 2 up");
        newModelTwo.setText("Mock text 2 up");
        newModelTwo.setLastUpdate(clockService.getUTCZonedDateTime().minusDays(1));
        newModelTwo.setLocalId(2);
        newModelTwo.setId(idTwo);
        data.add(newModelTwo);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setTitle("Mock title 3");
        modelThree.setFullText("Mock full text 3");
        modelThree.setSubtitle("Mock subtitle 3");
        modelThree.setText("Mock text 3");
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        modelThree.setLocalId(3);
        data.add(modelThree);

        final SynchronizableSaveSyncModel<Long, GlossaryItemDataModel> syncModel = new SynchronizableSaveSyncModel<>();
        syncModel.setSave(data);

        mvc.perform(put("/public/glossary/sync_save/")
                .content(JsonUtils.convertToJson(syncModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.length()").value("3"))
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 1 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.fullText=='Mock full text 1 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.subtitle=='Mock subtitle 1 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.text=='Mock text 1 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.localId=='1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.fullText=='Mock full text 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.subtitle=='Mock subtitle 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.text=='Mock text 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.localId=='2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 3')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.fullText=='Mock full text 3')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.subtitle=='Mock subtitle 3')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.text=='Mock text 3')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.localId=='3')]").isNotEmpty());
    }

    @Test
    public void syncFistUpdateSecondUpdatedOutdatedWithNewLocalIdThirdNew() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text 1");
        modelOne.setSubtitle("Mock subtitle 1");
        modelOne.setText("Mock text 1");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        modelOne.setLocalId(1);

        final Long idOne = this.saveItem(modelOne);

        final GlossaryItemDataModel newModelOne = new GlossaryItemDataModel();
        newModelOne.setTitle("Mock title 1 up");
        newModelOne.setFullText("Mock full text 1 up");
        newModelOne.setSubtitle("Mock subtitle 1 up");
        newModelOne.setText("Mock text 1 up");
        newModelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        newModelOne.setLocalId(1);
        newModelOne.setId(idOne);
        data.add(newModelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 2");
        modelTwo.setFullText("Mock full text 2");
        modelTwo.setSubtitle("Mock subtitle 2");
        modelTwo.setText("Mock text 2");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        modelTwo.setLocalId(2);

        final Long idTwo = this.saveItem(modelTwo);

        final GlossaryItemDataModel newModelTwo = new GlossaryItemDataModel();
        newModelTwo.setTitle("Mock title 2 up");
        newModelTwo.setFullText("Mock full text 2 up");
        newModelTwo.setSubtitle("Mock subtitle 2 up");
        newModelTwo.setText("Mock text 2 up");
        newModelTwo.setLastUpdate(clockService.getUTCZonedDateTime().minusDays(1));
        newModelTwo.setLocalId(3);
        newModelTwo.setId(idTwo);
        data.add(newModelTwo);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setTitle("Mock title 3");
        modelThree.setFullText("Mock full text 3");
        modelThree.setSubtitle("Mock subtitle 3");
        modelThree.setText("Mock text 3");
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        modelThree.setLocalId(3);
        data.add(modelThree);

        final SynchronizableSaveSyncModel<Long, GlossaryItemDataModel> syncModel = new SynchronizableSaveSyncModel<>();
        syncModel.setSave(data);

        mvc.perform(put("/public/glossary/sync_save/")
                .content(JsonUtils.convertToJson(syncModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.length()").value("3"))
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 1 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.fullText=='Mock full text 1 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.subtitle=='Mock subtitle 1 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.text=='Mock text 1 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.localId=='1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.fullText=='Mock full text 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.subtitle=='Mock subtitle 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.text=='Mock text 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.localId=='3')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 3')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.fullText=='Mock full text 3')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.subtitle=='Mock subtitle 3')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.text=='Mock text 3')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.localId=='3')]").isNotEmpty());
    }

    @Test
    public void trySyncWithoutLastUpdate() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text 1");
        modelOne.setSubtitle("Mock subtitle 1");
        modelOne.setText("Mock text 1");
        modelOne.setLocalId(1);
        data.add(modelOne);

        final SynchronizableSaveSyncModel<Long, GlossaryItemDataModel> syncModel = new SynchronizableSaveSyncModel<>();
        syncModel.setSave(data);

        mvc.perform(put("/public/glossary/sync_save/")
                .content(JsonUtils.convertToJson(syncModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void syncUpdateOneItemOfThree() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text 1");
        modelOne.setSubtitle("Mock subtitle 1");
        modelOne.setText("Mock text 1");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        modelOne.setLocalId(1);

        this.saveItem(modelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 2");
        modelTwo.setFullText("Mock full text 2");
        modelTwo.setSubtitle("Mock subtitle 2");
        modelTwo.setText("Mock text 2");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        modelTwo.setLocalId(2);

        this.saveItem(modelTwo);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setTitle("Mock title 3");
        modelThree.setFullText("Mock full text 3");
        modelThree.setSubtitle("Mock subtitle 3");
        modelThree.setText("Mock text 3");
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        modelThree.setLocalId(3);

        final Long idThree = this.saveItem(modelThree);

        final GlossaryItemDataModel newModelThree = new GlossaryItemDataModel();
        newModelThree.setTitle("Mock title 3 up");
        newModelThree.setFullText("Mock full text 3 up");
        newModelThree.setSubtitle("Mock subtitle 3 up");
        newModelThree.setText("Mock text 3 up");
        newModelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        newModelThree.setLocalId(4);
        newModelThree.setId(idThree);
        data.add(newModelThree);

        final SynchronizableSaveSyncModel<Long, GlossaryItemDataModel> syncModel = new SynchronizableSaveSyncModel<>();
        syncModel.setSave(data);

        mvc.perform(put("/public/glossary/sync_save/")
                .content(JsonUtils.convertToJson(syncModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.length()").value("3"))
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.fullText=='Mock full text 1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.subtitle=='Mock subtitle 1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.text=='Mock text 1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.fullText=='Mock full text 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.subtitle=='Mock subtitle 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.text=='Mock text 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 3 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.fullText=='Mock full text 3 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.subtitle=='Mock subtitle 3 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.text=='Mock text 3 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.localId=='4')]").isNotEmpty());
    }

    @Test
    public void syncUpdateZeroItems() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text 1");
        modelOne.setSubtitle("Mock subtitle 1");
        modelOne.setText("Mock text 1");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        modelOne.setLocalId(1);

        this.saveItem(modelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 2");
        modelTwo.setFullText("Mock full text 2");
        modelTwo.setSubtitle("Mock subtitle 2");
        modelTwo.setText("Mock text 2");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        modelTwo.setLocalId(2);

        this.saveItem(modelTwo);

        final GlossaryItemDataModel modelThree = new GlossaryItemDataModel();
        modelThree.setTitle("Mock title 3");
        modelThree.setFullText("Mock full text 3");
        modelThree.setSubtitle("Mock subtitle 3");
        modelThree.setText("Mock text 3");
        modelThree.setLastUpdate(clockService.getUTCZonedDateTime());
        modelThree.setLocalId(3);

        this.saveItem(modelThree);

        final SynchronizableSaveSyncModel<Long, GlossaryItemDataModel> syncModel = new SynchronizableSaveSyncModel<>();
        syncModel.setSave(data);

        mvc.perform(put("/public/glossary/sync_save/")
                .content(JsonUtils.convertToJson(syncModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.length()").value("3"))
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.fullText=='Mock full text 1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.subtitle=='Mock subtitle 1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.text=='Mock text 1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.fullText=='Mock full text 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.subtitle=='Mock subtitle 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.text=='Mock text 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 3')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.fullText=='Mock full text 3')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.subtitle=='Mock subtitle 3')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.text=='Mock text 3')]").isNotEmpty());
    }
    //</editor-fold>

    //<editor-fold desc="Sync with specific rules">
    @Test
    public void syncChangeTitleForExistentOne() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text 1");
        modelOne.setSubtitle("Mock subtitle 1");
        modelOne.setText("Mock text 1");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());

        this.saveItem(modelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 2");
        modelTwo.setFullText("Mock full text 2");
        modelTwo.setSubtitle("Mock subtitle 2");
        modelTwo.setText("Mock text 2");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());

        final Long idTwo = this.saveItem(modelTwo);

        final GlossaryItemDataModel newModelTwo = new GlossaryItemDataModel();
        newModelTwo.setTitle("Mock title 1");
        newModelTwo.setFullText("Mock full text 2 up");
        newModelTwo.setSubtitle("Mock subtitle 2 up");
        newModelTwo.setText("Mock text 2 up");
        newModelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        newModelTwo.setLocalId(1);
        newModelTwo.setId(idTwo);
        data.add(newModelTwo);

        final SynchronizableSaveSyncModel<Long, GlossaryItemDataModel> syncModel = new SynchronizableSaveSyncModel<>();
        syncModel.setSave(data);

        mvc.perform(put("/public/glossary/sync_save/")
                .content(JsonUtils.convertToJson(syncModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.length()").value("2"))
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.fullText=='Mock full text 1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.subtitle=='Mock subtitle 1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.text=='Mock text 1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.localId=='1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 1 [["+idTwo+"]]R')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.fullText=='Mock full text 2 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.subtitle=='Mock subtitle 2 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.text=='Mock text 2 up')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.localId=='1')]").isNotEmpty());
    }

    @Test
    public void syncSaveDuplicateTitlesSecondMoreUpdated() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text 1");
        modelOne.setSubtitle("Mock subtitle 1");
        modelOne.setText("Mock text 1");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime());
        modelOne.setLocalId(1);
        data.add(modelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 1");
        modelTwo.setFullText("Mock full text 2");
        modelTwo.setSubtitle("Mock subtitle 2");
        modelTwo.setText("Mock text 2");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime().plusMinutes(1));
        modelTwo.setLocalId(2);
        data.add(modelTwo);

        final SynchronizableSaveSyncModel<Long, GlossaryItemDataModel> syncModel = new SynchronizableSaveSyncModel<>();
        syncModel.setSave(data);

        mvc.perform(put("/public/glossary/sync_save/")
                .content(JsonUtils.convertToJson(syncModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.length()").value("1"))
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.fullText=='Mock full text 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.subtitle=='Mock subtitle 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.text=='Mock text 2')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.localId=='2')]").isNotEmpty());
    }

    @Test
    public void syncSaveDuplicateTitlesFirstMoreUpdated() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final List<GlossaryItemDataModel> data = new ArrayList<>();

        final GlossaryItemDataModel modelOne = new GlossaryItemDataModel();
        modelOne.setTitle("Mock title 1");
        modelOne.setFullText("Mock full text 1");
        modelOne.setSubtitle("Mock subtitle 1");
        modelOne.setText("Mock text 1");
        modelOne.setLastUpdate(clockService.getUTCZonedDateTime().plusMinutes(1));
        modelOne.setLocalId(1);
        data.add(modelOne);

        final GlossaryItemDataModel modelTwo = new GlossaryItemDataModel();
        modelTwo.setTitle("Mock title 1");
        modelTwo.setFullText("Mock full text 2");
        modelTwo.setSubtitle("Mock subtitle 2");
        modelTwo.setText("Mock text 2");
        modelTwo.setLastUpdate(clockService.getUTCZonedDateTime());
        modelTwo.setLocalId(2);
        data.add(modelTwo);

        final SynchronizableSaveSyncModel<Long, GlossaryItemDataModel> syncModel = new SynchronizableSaveSyncModel<>();
        syncModel.setSave(data);

        mvc.perform(put("/public/glossary/sync_save/")
                .content(JsonUtils.convertToJson(syncModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.length()").value("1"))
                .andExpect(jsonPath("$.data[?(@.title=='Mock title 1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.fullText=='Mock full text 1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.subtitle=='Mock subtitle 1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.text=='Mock text 1')]").isNotEmpty())
                .andExpect(jsonPath("$.data[?(@.localId=='1')]").isNotEmpty());
    }
    //</editor-fold>
}
