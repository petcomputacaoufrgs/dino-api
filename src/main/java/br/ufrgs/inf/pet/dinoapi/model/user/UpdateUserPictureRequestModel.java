package br.ufrgs.inf.pet.dinoapi.model.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static br.ufrgs.inf.pet.dinoapi.constants.AuthConstants.*;

public class UpdateUserPictureRequestModel {

    @NotNull(message = PICTURE_URL_NULL_MESSAGE)
    @Size(max = PICTURE_URL_MAX, message = PICTURE_URL_MESSAGE)
    private String pictureURL;

    public UpdateUserPictureRequestModel() {}

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }
}
