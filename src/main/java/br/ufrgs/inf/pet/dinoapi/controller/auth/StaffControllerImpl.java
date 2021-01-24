package br.ufrgs.inf.pet.dinoapi.controller.auth;

import br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableControllerImpl;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Staff;
import br.ufrgs.inf.pet.dinoapi.model.auth.staff.StaffDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.auth.StaffRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.StaffServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/staff/")
public class StaffControllerImpl extends SynchronizableControllerImpl<
        Staff, Long, StaffDataModel, StaffRepository, StaffServiceImpl> {

    @Autowired
    protected StaffControllerImpl(StaffServiceImpl service) {
        super(service);
    }

}
