package br.ufrgs.inf.pet.dinoapi.constants;

public final class ContactsConstants {
    public final static int NAME_MAX = 100;
    public final static String NAME_MAX_MESSAGE = "name should be between 1 and " + NAME_MAX;
    public final static String NAME_NULL_MESSAGE = "name cannot be null";

    public final static int DESCRIPTION_MAX = 500;
    public final static String DESCRIPTION_MAX_MESSAGE = "description should not be more than " + DESCRIPTION_MAX;

    public final static int NUMBER_MAX = 30;
    public final static String NUMBER_NULL_MESSAGE = "phone number cannot be null";
    public final static String NUMBER_MAX_MESSAGE = "number should be between 1 and " + NUMBER_MAX;

    public final static String TYPE_NULL_MESSAGE = "phone type cannot be null.";

    public final static String CONTACT_ID_NULL_MESSAGE = "contact id cannot be null";

    public final static String INVALID_CONTACT = "Invalid contact";

    public final static String INVALID_DECLINE_REQUEST = "User does not have Google Authentication";
    public final static String SUCCESS_DECLINE_REQUEST = "Success";
    public final static String SUCCESS_DECLINE_REQUEST_WITHOUT_ALERT= "Success, but server fail to alert another devices.";


    /*
    public final static List<ContactSaveModel> DEFAULT_CONTACTS = new ArrayList<ContactSaveModel>(Arrays.asList(
            new ContactSaveModel(1L, "SAMU",
                    new ArrayList<PhoneSaveModel>(Collections.singletonList(new PhoneSaveModel("192", (byte) 1))),
                    null,(byte) 1),
            new ContactSaveModel(2L, "Hospital de Clínicas (POA)",
                    new ArrayList<PhoneSaveModel>(Collections.singletonList(new PhoneSaveModel("(51) 3359-8000", (byte) 1))),
                    "O Hospital de Clínicas de Porto Alegre, popularmente conhecido por Clínicas, é uma instituição pública e universitária, ligada ao Ministério da Educação e à Universidade Federal do Rio Grande do Sul. Foi fundada em 1970, fruto do esforços empenhados na Faculdade de Medicina da UFRGS para sua construção.", (byte) 1))
    );
    */

}
