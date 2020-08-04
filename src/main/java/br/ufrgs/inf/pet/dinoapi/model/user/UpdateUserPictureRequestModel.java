package br.ufrgs.inf.pet.dinoapi.model.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateUserPictureRequestModel {

    @NotNull(message = "pictureURL cannot be null.")
    @Size(max = 500, message = "pictureURL should not be more than 500.")
    private String pictureURL;

    public UpdateUserPictureRequestModel() {}

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }
}
