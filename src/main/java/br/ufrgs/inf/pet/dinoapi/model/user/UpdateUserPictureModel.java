package br.ufrgs.inf.pet.dinoapi.model.user;

public class UpdateUserPictureModel {
    private String pictureURL;

    public UpdateUserPictureModel() {}

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }
}
