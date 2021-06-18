package br.ufrgs.inf.pet.dinoapi.utils;

import br.ufrgs.inf.pet.dinoapi.model.glossary.GlossaryItemDataModel;

import java.time.ZonedDateTime;

public class PatternUtils {

    public static String generateIdListPattern(Long id) {
        return "$.data[?(@==" + "'"+id.toString()+"')]";
    }

    public static String generateExistsPattern(GlossaryItemDataModel model) {
        return generateExistsPattern(model, false);
    }

    public static String generateExistsPattern(GlossaryItemDataModel model, Boolean testId) {
        return generateExistsPattern(model, testId, false);
    }

    public static String generateSyncExistsPattern(GlossaryItemDataModel model, Boolean testId) {
        return generateExistsPattern(model, testId, true);
    }

    public static String generateExistsPattern(GlossaryItemDataModel model, Boolean testId, Boolean testLocalId) {
        final ZonedDateTime lastUpdate = model.getLastUpdate();
        final String title = model.getTitle();
        final String text = model.getText();
        final String subtitle = model.getSubtitle();
        final String fullText = model.getFullText();
        String regex = "$.data[?(@.lastUpdate=~/^"+lastUpdate.toString()
                .substring(0, 19)+"([a-zA-Z0-9_.-]*)/i"+" && @.title=="+(title != null ? "'"+title+"'": "null") +
                " && @.text=="+(text != null ? "'"+text+"'": "null") + " && @.fullText=="+(fullText != null ? "'"+fullText+"'": "null") +
                " && @.subtitle=="+(subtitle != null ? "'"+subtitle+"'": "null");
        final String endRegex = ")]";
        if (testId) {
            final Long id = model.getId();

            regex = regex +" && @.id=="+(id != null ? "'"+id+"'": "null");
        }
        if (testLocalId) {
            final Integer localId = model.getLocalId();

            regex = regex +" && @.localId=="+(localId != null ? "'"+localId+"'": "null");
        }

        return regex + endRegex;
    }

}
