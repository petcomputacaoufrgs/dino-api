package br.ufrgs.inf.pet.dinoapi.controller;

import br.ufrgs.inf.pet.dinoapi.configuration.SpringTestConfig;
import br.ufrgs.inf.pet.dinoapi.controller.glossary.GlossaryControllerImpl;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableDeleteModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableGetModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.SynchronizableDataResponseModelImpl;
import br.ufrgs.inf.pet.dinoapi.repository.glossary.GlossaryItemRepository;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
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
public class GlossaryControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ClockServiceImpl clockService;

    @Autowired
    private GlossaryControllerImpl glossaryController;

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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.data.lastUpdate", matchesPattern("^"+model.getLastUpdate().toString().substring(0, 19)+"([a-zA-Z0-9_.-]*)")))
                .andExpect(jsonPath("$.data.title").value("Mock title"))
                .andExpect(jsonPath("$.data.fullText").value("Mock full text"))
                .andExpect(jsonPath("$.data.subtitle").value("Mock subtitle"))
                .andExpect(jsonPath("$.data.text").value(IsNull.nullValue()));
    }

    @Test
    public void trySaveItemWithoutLastUpdate() throws Exception {
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
        model.setLastUpdate(clockService.getUTCZonedDateTime());
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
        model.setTitle(null);
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
    public void tryDeleteItemWithoutSendingLasUpdate() throws Exception {
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
    //</editor-fold>

    //<editor-fold desc="Save all items">

    //</editor-fold>

    //<editor-fold desc="Get all items">

    //</editor-fold>

    //<editor-fold desc="Delete all items">

    //</editor-fold>
}
