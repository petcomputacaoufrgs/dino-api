package br.ufrgs.inf.pet.dinoapi.service.contact;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.Treatment;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.contacts.EssentialContactDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.contact.EssentialContactRepository;
import br.ufrgs.inf.pet.dinoapi.repository.treatment.TreatmentRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.topic.SynchronizableTopicMessageServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EssentialContactServiceImpl extends
        SynchronizableServiceImpl<EssentialContact, Long, Integer, EssentialContactDataModel, EssentialContactRepository> {

    private final TreatmentRepository treatmentRepository;


    public EssentialContactServiceImpl(TreatmentRepository treatmentRepository, EssentialContactRepository repository,
                                       AuthServiceImpl authService, ClockServiceImpl clock,
                                       SynchronizableTopicMessageServiceImpl<Long, Integer, EssentialContactDataModel> synchronizableTopicMessageService, LogAPIErrorServiceImpl logAPIErrorService) {
        super(repository, authService, clock, synchronizableTopicMessageService, logAPIErrorService);
        this.treatmentRepository = treatmentRepository;
    }

    @Override
    public EssentialContactDataModel convertEntityToModel(EssentialContact entity) {
        EssentialContactDataModel model = new EssentialContactDataModel();
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setColor(entity.getColor());
        if(entity.getTreatment() != null)
            model.setTreatmentId(entity.getTreatment().getId());
        return model;
    }

    @Override
    public EssentialContact convertModelToEntity(EssentialContactDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {

        EssentialContact entity = new EssentialContact();

        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        entity.setColor(model.getColor());
        searchTreatment(entity, model.getTreatmentId());

        return entity;
    }

    @Override
    public void updateEntity(EssentialContact entity, EssentialContactDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {

        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        entity.setColor(model.getColor());
    }

    @Override
    public Optional<EssentialContact> getEntityByIdAndUserAuth(Long id, Auth auth) throws AuthNullException {
        return this.repository.findById(id);
    }

    @Override
    public List<EssentialContact> getEntitiesByUserAuth(Auth auth) throws AuthNullException {
        return (List<EssentialContact>) this.repository.findAll();
    }

    @Override
    public List<EssentialContact> getEntitiesByIdsAndUserAuth(List<Long> ids, Auth auth) throws AuthNullException {
        return (List<EssentialContact>) this.repository.findAllById(ids);
    }

    @Override
    public List<EssentialContact> getEntitiesByUserAuthExceptIds(Auth auth, List<Long> ids) throws AuthNullException {
        return (List<EssentialContact>) this.repository.findAllById(ids);
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return WebSocketDestinationsEnum.ESSENTIAL_CONTACT;
    }

    private void searchTreatment (EssentialContact entity, Long treatmentId) throws ConvertModelToEntityException {
        if(treatmentId != null) {
            final Optional<Treatment> treatmentSearch = treatmentRepository.findById(treatmentId);
            if(treatmentSearch.isPresent()) {
                entity.setTreatment(treatmentSearch.get());
            } else {
                throw new ConvertModelToEntityException("Nop!");
            }
        }
    }

}
