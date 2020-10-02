package br.ufrgs.inf.pet.dinoapi.model.notes.sync;

import br.ufrgs.inf.pet.dinoapi.model.notes.NoteColumnResponseModel;

import java.util.List;

public class NoteColumnSyncResponse {
    private List<NoteColumnResponseModel> columns;

    private Long version;

    public List<NoteColumnResponseModel> getColumns() {
        return columns;
    }

    public void setColumns(List<NoteColumnResponseModel> columns) {
        this.columns = columns;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
