package br.ufrgs.inf.pet.dinoapi.model.glossary_version;

/**
 * Model para envio da versão do glossário
 *
 * @author joao.silva
 */
public class GlossaryVersionResponseModel {
    Long version;

    public GlossaryVersionResponseModel(){}

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}

