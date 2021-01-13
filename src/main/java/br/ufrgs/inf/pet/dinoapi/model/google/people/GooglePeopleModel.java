package br.ufrgs.inf.pet.dinoapi.model.google.people;

import java.util.List;

public class GooglePeopleModel {
    private List<GooglePeopleNameModel> names;
    private List<GooglePeoplePhoneNumberModel> phoneNumbers;
    private List<GooglePeopleBiographiesModel> biographies;
    private String resourceName;
    private String etag;

    public List<GooglePeopleNameModel> getNames() {
        return names;
    }

    public void setNames(List<GooglePeopleNameModel> names) {
        this.names = names;
    }

    public List<GooglePeoplePhoneNumberModel> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<GooglePeoplePhoneNumberModel> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public List<GooglePeopleBiographiesModel> getBiographies() {
        return biographies;
    }

    public void setBiographies(List<GooglePeopleBiographiesModel> biographies) {
        this.biographies = biographies;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }
}
