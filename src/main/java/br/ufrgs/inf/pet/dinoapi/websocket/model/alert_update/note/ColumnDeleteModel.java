package br.ufrgs.inf.pet.dinoapi.websocket.model.alert_update.note;

import java.util.List;

public class ColumnDeleteModel {
    private List<String> titleList;

    private Long newVersion;

    public List<String> getTitleList() {
        return titleList;
    }

    public void setTitleList(List<String> titleList) {
        this.titleList = titleList;
    }

    public Long getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(Long newVersion) {
        this.newVersion = newVersion;
    }
}
