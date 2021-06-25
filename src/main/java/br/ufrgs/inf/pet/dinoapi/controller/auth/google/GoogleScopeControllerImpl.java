package br.ufrgs.inf.pet.dinoapi.controller.auth.google;

import br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableControllerImpl;
import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleScope;
import br.ufrgs.inf.pet.dinoapi.model.auth.google.GoogleScopeDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.auth.google.GoogleScopeRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.google.GoogleScopeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.ufrgs.inf.pet.dinoapi.constants.PathConstants.GOOGLE_SCOPE;

@RestController
@RequestMapping(GOOGLE_SCOPE)
public class GoogleScopeControllerImpl extends SynchronizableControllerImpl<
        GoogleScope, Long, GoogleScopeDataModel, GoogleScopeRepository, GoogleScopeServiceImpl> {

    @Autowired
    protected GoogleScopeControllerImpl(GoogleScopeServiceImpl service) {
        super(service);
    }
}
