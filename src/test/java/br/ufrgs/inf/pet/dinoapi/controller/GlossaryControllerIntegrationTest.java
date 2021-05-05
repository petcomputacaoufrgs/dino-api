package br.ufrgs.inf.pet.dinoapi.controller;

import br.ufrgs.inf.pet.dinoapi.configuration.SpringTestConfig;
import br.ufrgs.inf.pet.dinoapi.constants.GlossaryConstants;
import br.ufrgs.inf.pet.dinoapi.controller.glossary.GlossaryControllerImpl;
import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryItemDataModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableGetModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.response.SynchronizableDataResponseModelImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.utils.JsonMapperUtils;
import br.ufrgs.inf.pet.dinoapi.utils.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static br.ufrgs.inf.pet.dinoapi.utils.PatternUtils.generateExistsPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringTestConfig.class
)
@TestPropertySource(locations="classpath:test.properties")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class GlossaryControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ClockServiceImpl clockService;

    @Autowired
    private GlossaryControllerImpl glossaryController;

    static final String SAVE_PATH = "/public/glossary/save/";
    static final String GET_PATH = "/public/glossary/get/";

    private void saveItem(GlossaryItemDataModel model) throws Exception {
        final ResponseEntity<SynchronizableDataResponseModelImpl<Long, GlossaryItemDataModel>> result = glossaryController.save(model);
        if (result.getBody() != null && result.getBody().getData() != null && result.getBody().getData().getId() != null) {
            final Long id = result.getBody().getData().getId();
            model.setId(id);
        } else {
            throw new Exception("Fail to save GlossaryItemDataModel on API");
        }
    }

    //<editor-fold desc="Save item">
    @Test
    public void saveBasicItem() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("M".repeat(GlossaryConstants.TITLE_MAX));
        model.setSubtitle("M".repeat(GlossaryConstants.SUBTITLE_MAX));
        model.setText("M".repeat(GlossaryConstants.TEXT_MAX));
        model.setFullText("M".repeat(GlossaryConstants.FULLTEXT_MAX));
        model.setLastUpdate(clockService.getUTCZonedDateTime());
        mvc.perform(post(SAVE_PATH)
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath(generateExistsPattern(model)).isNotEmpty());
    }

    // SAVE: ATTRIBUTES NULL VALUES ------------------------------------------------------------------------------------

    @Test
    public void whenSaveItem_AndTitleValueNull_ThenStatus400() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setSubtitle("Mock subtitle");
        model.setText("Mock text");
        model.setFullText("Mock fulltext");
        model.setLastUpdate(clockService.getUTCZonedDateTime());
        mvc.perform(post(SAVE_PATH)
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenSaveItem_AndFullTextValueNull_ThenStatus200() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title");
        model.setSubtitle("Mock subtitle");
        model.setText("Mock text");
        model.setLastUpdate(clockService.getUTCZonedDateTime());
        mvc.perform(post(SAVE_PATH)
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath(generateExistsPattern(model)).isNotEmpty());
    }

    @Test
    public void whenSaveItem_AndSubtitleValueNull_ThenStatus200() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title");
        model.setFullText("Mock full text");
        model.setText("Mock text");
        model.setLastUpdate(clockService.getUTCZonedDateTime());
        mvc.perform(post(SAVE_PATH)
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath(generateExistsPattern(model)).isNotEmpty());
    }

    @Test
    public void whenSaveItem_AndTextValueNull_ThenStatus400() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title");
        model.setFullText("Mock full text");
        model.setSubtitle("Mock subtitle");
        model.setLastUpdate(clockService.getUTCZonedDateTime());
        mvc.perform(post(SAVE_PATH)
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenSaveItem_AndLastUpdateValueNull_ThenStatus400() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title");
        model.setFullText("Mock full text");
        model.setSubtitle("Mock subtitle");
        model.setText("Mock text");
        mvc.perform(post(SAVE_PATH)
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // SAVE: ATTRIBUTES BLANK VALUES -----------------------------------------------------------------------------------

    @Test
    public void whenSaveItem_AndTitleBlank_ThenStatus400() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle(" ");
        model.setFullText("Mock full text");
        model.setSubtitle("Mock subtitle");
        model.setText("Mock text");
        mvc.perform(post(SAVE_PATH)
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenSaveItem_AndTextBlank_ThenStatus400() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title");
        model.setFullText("Mock full text");
        model.setSubtitle("Mock subtitle");
        model.setText(" ");
        mvc.perform(post(SAVE_PATH)
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // SAVE: ATTRIBUTES MAX LENGTH EXCEEDED ----------------------------------------------------------------------------

    @Test
    public void whenSaveItem_AndTitleLengthGreaterThanMax_ThenStatus400() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("M".repeat(GlossaryConstants.TITLE_MAX + 1));
        model.setFullText("Mock full text");
        model.setSubtitle("Mock subtitle");
        model.setText("Mock text");
        model.setLastUpdate(clockService.getUTCZonedDateTime());
        mvc.perform(post(SAVE_PATH)
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenSaveItem_AndTextLengthGreaterThanMax_ThenStatus400() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title");
        model.setFullText("Mock full text");
        model.setSubtitle("Mock subtitle");
        model.setText("M".repeat(GlossaryConstants.TEXT_MAX + 1));
        model.setLastUpdate(clockService.getUTCZonedDateTime());
        mvc.perform(post(SAVE_PATH)
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenSaveItem_AndSubtitleLengthGreaterThanMax_ThenStatus400() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title");
        model.setFullText("Mock full text");
        model.setSubtitle("M".repeat(GlossaryConstants.SUBTITLE_MAX + 1));
        model.setText("Mock text");
        model.setLastUpdate(clockService.getUTCZonedDateTime());
        mvc.perform(post(SAVE_PATH)
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenSaveItem_AndFullTextLengthGreaterThanMax_ThenStatus400() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title");
        model.setSubtitle("Mock subtitle");
        model.setText("Mock text");
        model.setFullText("M".repeat(GlossaryConstants.FULLTEXT_MAX + 1));
        model.setLastUpdate(clockService.getUTCZonedDateTime());
        mvc.perform(post(SAVE_PATH)
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // SAVE: ALL POSSIBLE ATTRIBUTES VALUE NULL ------------------------------------------------------------------------

    @Test
    public void whenSaveItem_AndSubtitleValueNull_AndTextValueNull_ThenStatus200() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel model = new GlossaryItemDataModel();
        model.setTitle("Mock title");
        model.setSubtitle(null);
        model.setText("Mock text");
        model.setFullText(null);
        model.setLastUpdate(clockService.getUTCZonedDateTime());
        mvc.perform(post(SAVE_PATH)
                .content(JsonUtils.convertToJson(model, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath(generateExistsPattern(model)).isNotEmpty());
    }

    //</editor-fold>

    //<editor-fold desc="Save with specific Glossary rules">
    @Test
    public void whenUpdateItem_AndUsingSameTitle_AndWithoutId_ThenStatus200() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText("Mock full text");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());
        this.saveItem(saveModel);

        final GlossaryItemDataModel updateModel = new GlossaryItemDataModel();
        updateModel.setTitle("Mock title");
        updateModel.setFullText("Mock full text updated");
        updateModel.setSubtitle("Mock subtitle up");
        updateModel.setText("Mock text updated");
        updateModel.setLastUpdate(clockService.getUTCZonedDateTime());

        mvc.perform(post(SAVE_PATH)
                .content(JsonUtils.convertToJson(updateModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath(generateExistsPattern(updateModel, false)).isNotEmpty());

        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(saveModel.getId());
        updateModel.setId(saveModel.getId());

        mvc.perform(get(GET_PATH)
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath(generateExistsPattern(updateModel, true)).isNotEmpty());
    }

    @Test
    public void whenUpdateItem_AndUsingSameTitle_AndWithoutId_AndOneMonthMoreUpdated_ThenStatus200() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setFullText("Mock full text");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        this.saveItem(saveModel);

        final GlossaryItemDataModel updateModel = new GlossaryItemDataModel();
        updateModel.setTitle("Mock title");
        updateModel.setFullText("Mock full text updated");
        updateModel.setSubtitle("Mock subtitle up");
        updateModel.setText("Mock text updated");
        updateModel.setLastUpdate(clockService.getUTCZonedDateTime().plusMonths(1));

        mvc.perform(post(SAVE_PATH)
                .content(JsonUtils.convertToJson(updateModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath(generateExistsPattern(updateModel, false)).isNotEmpty());

        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(saveModel.getId());
        updateModel.setId(saveModel.getId());

        mvc.perform(get(GET_PATH)
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath(generateExistsPattern(updateModel, true)).isNotEmpty());
    }

    @Test
    public void updateItemUsingSameTitleWithoutIdAndNullAttributes() throws Exception {
        final ObjectMapper mapper = JsonMapperUtils.clientObjectMapper();
        final GlossaryItemDataModel saveModel = new GlossaryItemDataModel();
        saveModel.setTitle("Mock title");
        saveModel.setSubtitle("Mock subtitle");
        saveModel.setText("Mock text");
        saveModel.setFullText("Mock full text");
        saveModel.setLastUpdate(clockService.getUTCZonedDateTime());

        this.saveItem(saveModel);

        final GlossaryItemDataModel updateModel = new GlossaryItemDataModel();
        updateModel.setTitle("Mock title");
        updateModel.setSubtitle(null);
        updateModel.setText("Mock text updated");
        updateModel.setFullText(null);
        updateModel.setLastUpdate(clockService.getUTCZonedDateTime().plusMonths(1));

        mvc.perform(post(SAVE_PATH)
                .content(JsonUtils.convertToJson(updateModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath(generateExistsPattern(updateModel, false)).isNotEmpty());

        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(saveModel.getId());
        updateModel.setId(saveModel.getId());

        mvc.perform(get(GET_PATH)
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath(generateExistsPattern(updateModel, true)).isNotEmpty());
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

        this.saveItem(saveModel);

        final GlossaryItemDataModel updateModel = new GlossaryItemDataModel();
        updateModel.setTitle("Mock title");
        updateModel.setFullText("Mock full text updated");
        updateModel.setSubtitle("Mock subtitle up");
        updateModel.setText("Mock text updated");
        updateModel.setLastUpdate(clockService.getUTCZonedDateTime().minusDays(1));

        mvc.perform(post(SAVE_PATH)
                .content(JsonUtils.convertToJson(updateModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath(generateExistsPattern(saveModel, true)).isNotEmpty());

        final SynchronizableGetModel<Long> getModel = new SynchronizableGetModel<>();
        getModel.setId(saveModel.getId());

        mvc.perform(get(GET_PATH)
                .content(JsonUtils.convertToJson(getModel, mapper))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.error").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.errorCode").value(IsNull.nullValue()))
                .andExpect(jsonPath(generateExistsPattern(saveModel, true)).isNotEmpty());
    }
    //</editor-fold>
}