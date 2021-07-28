package br.ufrgs.inf.pet.dinoapi.service.auth;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.auth.Staff;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.enumerable.PermissionEnum;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.ConvertModelToEntityException;
import br.ufrgs.inf.pet.dinoapi.model.auth.staff.StaffDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.auth.StaffRepository;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.SynchronizableAdminMessageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class StaffServiceImpl extends SynchronizableServiceImpl<Staff, Long, StaffDataModel, StaffRepository> {

    private final UserServiceImpl userService;

    public StaffServiceImpl(StaffRepository repository, AuthServiceImpl authService, ClockServiceImpl clock,
                            SynchronizableAdminMessageService<Long, StaffDataModel> synchronizableAdminMessageService,
                            LogAPIErrorServiceImpl logAPIErrorService, UserServiceImpl userService) {
        super(repository, authService, clock, synchronizableAdminMessageService, logAPIErrorService);
        this.userService = userService;
    }

    @Override
    public List<PermissionEnum> getNecessaryPermissionsToEdit() {
        return Collections.singletonList(PermissionEnum.ADMIN);
    }

    @Override
    public StaffDataModel convertEntityToModel(Staff entity) {
        final StaffDataModel model = new StaffDataModel();
        model.setEmail(entity.getEmail());
        model.setSentInvitationDate(clock.toUTCZonedDateTime(entity.getSentInvitationDate()));
        model.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);

        return model;
    }

    @Override
    public Staff convertModelToEntity(StaffDataModel model, Auth auth) throws ConvertModelToEntityException, AuthNullException {
        final Staff entity = new Staff();
        entity.setEmail(model.getEmail());
        entity.setSentInvitationDate(model.getSentInvitationDate().toLocalDateTime());

        if(model.getUserId() != null) {
            final User user = userService.findUserById(model.getUserId());
            entity.setUser(user);
        }

        return entity;
    }

    @Override
    public void updateEntity(Staff entity, StaffDataModel model, Auth auth) {
        entity.setEmail(model.getEmail());
        entity.setSentInvitationDate(model.getSentInvitationDate().toLocalDateTime());

        if(model.getUserId() != null) {
            final User user = userService.findUserById(model.getUserId());
            entity.setUser(user);
        }
    }

    @Override
    public Optional<Staff> findEntityByIdThatUserCanRead(Long id, Auth auth) throws AuthNullException {
        return this.repository.findById(id);
    }

    @Override
    public Optional<Staff> findEntityByIdThatUserCanEdit(Long id, Auth auth) throws AuthNullException {
        return this.repository.findById(id);
    }

    @Override
    public List<Staff> findEntitiesThatUserCanRead(Auth auth) throws AuthNullException {
        return (List<Staff>) this.repository.findAll();
    }

    @Override
    public List<Staff> findEntitiesByIdThatUserCanEdit(List<Long> ids, Auth auth) throws AuthNullException {
        return (List<Staff>) this.repository.findAllById(ids);
    }

    @Override
    public List<Staff> findEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> ids) throws AuthNullException {
        return this.repository.findAllExcludingIds(ids);
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return WebSocketDestinationsEnum.STAFF;
    }

    public Staff findStaffByEmail(String email) {
        if (email != null) {
            final Optional<Staff> queryResult = this.repository.findByEmail(email);
            if (queryResult.isPresent()) {
                return queryResult.get();
            }
        }

        return null;
    }

    public void updateStaffUser(Staff staff, User user) {
        final Auth fakeAuth = new Auth();
        fakeAuth.setUser(user);
        staff.setUser(user);
        staff.setLastUpdate(LocalDateTime.now());
        try {
            this.internalSave(this.completeConvertEntityToModel(staff), fakeAuth);
        } catch (AuthNullException | ConvertModelToEntityException e) {
            this.logAPIError(e);
        }
    }
}
