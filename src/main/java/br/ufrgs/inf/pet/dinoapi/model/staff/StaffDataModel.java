package br.ufrgs.inf.pet.dinoapi.model.staff;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

public class StaffDataModel extends SynchronizableDataLocalIdModel<Long> {

    @NotNull
    @Size(min = 1, max = 50)
    private String email;

    @NotNull
    private ZonedDateTime sentInvitationDate;

    private Long userId;

    public StaffDataModel() { }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ZonedDateTime getSentInvitationDate() {
        return sentInvitationDate;
    }

    public void setSentInvitationDate(ZonedDateTime sentInvitationDate) {
        this.sentInvitationDate = sentInvitationDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
