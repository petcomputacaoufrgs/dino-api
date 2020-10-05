package br.ufrgs.inf.pet.dinoapi.model.note.sync.column;

import br.ufrgs.inf.pet.dinoapi.constants.NoteColumnConstants;
import br.ufrgs.inf.pet.dinoapi.model.note.save.NoteColumnSaveRequestModel;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class NoteColumnSyncRequestModel {
    @Valid
    @NotNull(message = NoteColumnConstants.CHANGED_COLUMNS_NULL_MESSAGE)
    private List<NoteColumnSyncChangedRequestModel> changedColumns;

    @Valid
    @NotNull(message = NoteColumnConstants.NEW_COLUMNS_NULL_MESSAGE)
    private List<NoteColumnSaveRequestModel> newColumns;

    @Valid
    @NotNull(message = NoteColumnConstants.DELETED_COLUMNS_NULL_MESSAGE)
    private List<NoteColumnSyncDeleteRequestModel> deletedColumns;

    @Valid
    @NotNull(message = NoteColumnConstants.COLUMN_ORDER_NULL_MESSAGE)
    private List<NoteColumnSyncOrderRequestModel> columnsOrder;

    public List<NoteColumnSyncChangedRequestModel> getChangedColumns() {
        return changedColumns;
    }

    public void setChangedColumns(List<NoteColumnSyncChangedRequestModel> changedColumns) {
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

    public List<NoteColumnSyncOrderRequestModel> getColumnsOrder() {
        return columnsOrder;
    }

    public void setColumnsOrder(List<NoteColumnSyncOrderRequestModel> columnsOrder) {
        this.columnsOrder = columnsOrder;
    }
}
