package br.ufrgs.inf.pet.dinoapi.model.contacts;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.Phone;

public class PhoneModel {
    private Long id;
    private String countryCode;
    private String areaCode;
    private String number;

    /*
    1: special short public utility numbers (see below)
    2 to 5: landlines
    6 to 9: mobile phones

    100: Human Rights Secretariat
    112: emergency number in European countries (redirects to 190)
    128: standard emergency number in Mercosul (in Brazil, redirects to 190)
    136: Ministry of Health hotline
    147: Digital television transition hotline (2010–2023)
    153: Municipal Guards
    181: anonymous crime reporting (some areas only, others may use different, more miscellaneous numbers)
    188: Centro de Valorização da Vida (Suicide prevention helpline)
    190: Military Police
    191: Federal Highway Police
    192: ambulance
    193: firefighters
    194: Federal Police Department
    197: Civil Police
    198: state Highway Patrol
    199: Civil Defense
    */
    public PhoneModel(Phone phone) {
        this.id = phone.getId();
        this.countryCode = phone.getCountryCode();
        this.areaCode = phone.getAreaCode();
        this.number = phone.getNumber();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
