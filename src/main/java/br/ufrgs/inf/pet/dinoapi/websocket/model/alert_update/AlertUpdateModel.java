package br.ufrgs.inf.pet.dinoapi.websocket.model.alert_update;

public class AlertUpdateModel {
    private Long newVersion;

    private Long newId;

    public Long getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(Long newVersion) {
        this.newVersion = newVersion;
    }

    public Long getNewId() {
        return newId;
    }

    public void setNewId(Long newId) {
        this.newId = newId;
    }
}
