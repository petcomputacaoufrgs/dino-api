package br.ufrgs.inf.pet.dinoapi.model.glossary_version;

import java.util.Date;

/**
 * Model para envio da versão do glossário
 *
 * @author joao.silva
 */
public class GlossaryVersionModel {
    Long version;

    public GlossaryVersionModel(){}

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}

