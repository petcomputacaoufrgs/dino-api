package br.ufrgs.inf.pet.dinoapi.model.report;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static br.ufrgs.inf.pet.dinoapi.constants.ReportConstants.*;

public class ReportDataModel extends SynchronizableDataLocalIdModel<Long> {

    @NotBlank(message = REPORT_BLANK_MESSAGE)
    @Size(max = WHAT_HOW_MAX, message = WHAT_HOW_SIZE_MESSAGE)
    private String what;

    @Size(max = WHAT_HOW_MAX, message = WHAT_HOW_SIZE_MESSAGE)
    private String how;

    @Size(max = WHERE_MAX, message = WHERE_SIZE_MESSAGE)
    private String where;

    private Long userId;

    public ReportDataModel() {}

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public String getHow() {
        return how;
    }

    public void setHow(String how) {
        this.how = how;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
