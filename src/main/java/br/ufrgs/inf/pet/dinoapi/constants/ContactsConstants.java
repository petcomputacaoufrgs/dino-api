package br.ufrgs.inf.pet.dinoapi.constants;

import br.ufrgs.inf.pet.dinoapi.model.contacts.ContactSaveModel;
import br.ufrgs.inf.pet.dinoapi.model.contacts.PhoneSaveModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ContactsConstants {
    public final static String ID_NULL_MESSAGE = "id cannot be null.";

    public final static int NAME_MAX = 100;
    public final static String NAME_MAX_MESSAGE = "name should be between 1 and MAX.";
    public final static String NAME_NULL_MESSAGE = "O nome do contato não pode ser nulo.";

    public final static int DESCRIPTION_MAX = 500;
    public final static String DESCRIPTION_MAX_MESSAGE = "description should not be more than MAX.";

    public final static int NUMBER_MAX = 30;
    public final static String NUMBER_NULL_MESSAGE = "O número do contato não pode ser nulo.";
    public final static String NUMBER_MESSAGE = "number should be between 1 and MAX.";

    public final static int TYPE_MAX = 9;
    public final static String TYPE_NULL_MESSAGE = "O tipo de número do contato não pode ser nulo.";
    public final static String TYPE_MESSAGE = "type should be between 1 and MAX";

    public final static List<ContactSaveModel> DEFAULT_CONTACTS = new ArrayList<ContactSaveModel>(Arrays.asList(
            new ContactSaveModel(1L, "SAMU",
                    new ArrayList<PhoneSaveModel>(Collections.singletonList(new PhoneSaveModel("192", (byte) 1))),
                    null,(byte) 1),
            new ContactSaveModel(2L, "Hospital de Clínicas (POA)",
                    new ArrayList<PhoneSaveModel>(Collections.singletonList(new PhoneSaveModel("(51) 3359-8000", (byte) 1))),
                    "O Hospital de Clínicas de Porto Alegre, popularmente conhecido por Clínicas, é uma instituição pública e universitária, ligada ao Ministério da Educação e à Universidade Federal do Rio Grande do Sul. Foi fundada em 1970, fruto do esforços empenhados na Faculdade de Medicina da UFRGS para sua construção.", (byte) 1))
    );
}
