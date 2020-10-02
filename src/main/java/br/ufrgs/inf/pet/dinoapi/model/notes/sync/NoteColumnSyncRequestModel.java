package br.ufrgs.inf.pet.dinoapi.model.notes.sync;

import br.ufrgs.inf.pet.dinoapi.model.notes.NoteColumnChangedRequestModel;
import br.ufrgs.inf.pet.dinoapi.model.notes.NoteColumnSaveRequestModel;

import javax.validation.Valid;
import java.util.List;

public class NoteColumnSyncRequestModel {
    @Valid
    private List<NoteColumnChangedRequestModel> changedColumns;

    @Valid
    private List<NoteColumnSaveRequestModel> newColumns;

    @Valid
    private List<NoteColumnSyncDeleteRequestModel> deletedColumns;

    @Valid
    private List<NoteColumnSyncOrderRequestModel> orderColumns;

    public List<NoteColumnChangedRequestModel> getChangedColumns() {
        return changedColumns;
    }

    public void setChangedColumns(List<NoteColumnChangedRequestModel> changedColumns) {
        this.changedColumns = changedColumns;
    }

    public List<NoteColumnSaveRequestModel> getNewColumns() {
        return newColumns;
    }

    public void setNewColumns(List<NoteColumnSaveRequestModel> newColumns) {
        this.newColumns = newColumns;
    }

    public List<NoteColumnSyncDeleteRequestModel> getDeletedColumns() {
        return deletedColumns;
    }

    public void setDeletedColumns(List<NoteColumnSyncDeleteRequestModel> deletedColumns) {
        this.deletedColumns = deletedColumns;
    }

    public List<NoteColumnSyncOrderRequestModel> getOrderColumns() {
        return orderColumns;
    }

    public void setOrderColumns(List<NoteColumnSyncOrderRequestModel> orderColumns) {
        this.orderColumns = orderColumns;
    }
}
