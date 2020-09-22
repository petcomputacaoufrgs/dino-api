package br.ufrgs.inf.pet.dinoapi.websocket.model.alert_update.note;

import java.util.List;

public class NoteDeleteModel {
    private List<Long> idList;

    private Long newVersion;

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }

    public Long getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(Long newVersion) {
        this.newVersion = newVersion;
    }
}
