package br.ufrgs.inf.pet.dinoapi.controller.treatment;

import br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableControllerImpl;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.Treatment;
import br.ufrgs.inf.pet.dinoapi.model.treatment.TreatmentDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.treatment.TreatmentRepository;
import br.ufrgs.inf.pet.dinoapi.service.treatment.TreatmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/treatment/")
public class TreatmentControllerImpl extends SynchronizableControllerImpl<
        Treatment, Long, TreatmentDataModel, TreatmentRepository, TreatmentServiceImpl> {

    @Autowired
    protected TreatmentControllerImpl(TreatmentServiceImpl service) {
        super(service);
    }
}
