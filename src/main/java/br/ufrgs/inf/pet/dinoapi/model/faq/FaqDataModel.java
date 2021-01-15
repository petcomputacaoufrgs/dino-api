package br.ufrgs.inf.pet.dinoapi.model.faq;

import br.ufrgs.inf.pet.dinoapi.constants.FaqConstants;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FaqDataModel extends SynchronizableDataLocalIdModel<Long> {
    @NotNull(message = FaqConstants.TITLE_NULL_MESSAGE)
    @Size(min = FaqConstants.TITLE_MIN, max = FaqConstants.TITLE_MAX, message = FaqConstants.TITLE_MAX_MESSAGE)
    private String title;

    @NotNull(message = FaqConstants.TREATMENT_ID_NULL_MESSAGE)
    private Long treatmentId;

    public FaqDataModel() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(Long treatmentId) {
        this.treatmentId = treatmentId;
    }
}
