package br.ufrgs.inf.pet.dinoapi.controller.staff;

import br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableControllerImpl;
import br.ufrgs.inf.pet.dinoapi.entity.staff.Staff;
import br.ufrgs.inf.pet.dinoapi.model.staff.StaffDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.staff.StaffRepository;
import br.ufrgs.inf.pet.dinoapi.service.staff.StaffServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.ufrgs.inf.pet.dinoapi.constants.PathConstants.STAFF;

@RestController
@RequestMapping(STAFF)
public class StaffControllerImpl extends SynchronizableControllerImpl<
        Staff, Long, StaffDataModel, StaffRepository, StaffServiceImpl> {

    @Autowired
    protected StaffControllerImpl(StaffServiceImpl service) {
        super(service);
    }

}
