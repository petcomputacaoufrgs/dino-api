package br.ufrgs.inf.pet.dinoapi.model.user;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static br.ufrgs.inf.pet.dinoapi.constants.UserConstants.*;

public class UserDataModel extends SynchronizableDataLocalIdModel<Long> {
    private String name;

    private String email;

    @NotNull
    int permission;

    @NotNull(message = PICTURE_URL_NULL_MESSAGE)
    @Size(max = PICTURE_URL_MAX, message = PICTURE_URL_MAX_MESSAGE)
    private String pictureURL;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }
}
