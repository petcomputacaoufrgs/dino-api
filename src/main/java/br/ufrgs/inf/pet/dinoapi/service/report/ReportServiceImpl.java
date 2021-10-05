package br.ufrgs.inf.pet.dinoapi.service.report;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.report.Report;
import br.ufrgs.inf.pet.dinoapi.exception.synchronizable.AuthNullException;
import br.ufrgs.inf.pet.dinoapi.model.report.ReportDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.report.ReportRepository;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.clock.ClockServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.synchronizable.SynchronizableServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.user.UserServiceImpl;
import br.ufrgs.inf.pet.dinoapi.websocket.enumerable.WebSocketDestinationsEnum;
import br.ufrgs.inf.pet.dinoapi.websocket.service.SynchronizableMessageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportServiceImpl extends SynchronizableServiceImpl<Report, Long, ReportDataModel, ReportRepository> {

    private final UserServiceImpl userService;

    public ReportServiceImpl(UserServiceImpl userService, ReportRepository repository, AuthServiceImpl authService, ClockServiceImpl clock, SynchronizableMessageService<Long, ReportDataModel> synchronizableTopicMessageService, LogAPIErrorServiceImpl logAPIErrorService) {
        super(repository, authService, clock, synchronizableTopicMessageService, logAPIErrorService);
        this.userService = userService;
    }

    @Override
    public ReportDataModel convertEntityToModel(Report entity) {
        final ReportDataModel model = new ReportDataModel();
        model.setWhat(entity.getWhat());
        model.setHow(entity.getHow());
        model.setWhere(entity.getWhere());
        model.setUserId(entity.getUser().getId());

        return model;
    }

    @Override
    public Report convertModelToEntity(ReportDataModel model, Auth auth) throws AuthNullException {
        if (auth != null) {
            final Report entity = new Report();
            entity.setWhat(model.getWhat());
            entity.setHow(model.getHow());
            entity.setWhere(model.getWhere());
            entity.setUser(auth.getUser());

            return entity;
        }

        throw new AuthNullException();
    }

    @Override
    public void updateEntity(Report entity, ReportDataModel model, Auth auth) throws AuthNullException {
        if (auth != null) {
            entity.setWhat(model.getWhat());
            entity.setHow(model.getHow());
            entity.setWhere(model.getWhere());
        } else throw new AuthNullException();
    }

    @Override
    public Optional<Report> findEntityByIdThatUserCanRead(Long id, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return authService.isStaffOrAdmin()
                ? this.repository.findById(id)
                : this.repository.findByIdAndUserId(id, auth.getUser().getId());
    }

    @Override
    public Optional<Report> findEntityByIdThatUserCanEdit(Long id, Auth auth) throws AuthNullException {
        return findEntityByIdThatUserCanRead(id, auth);
    }

    @Override
    public List<Report> findEntitiesThatUserCanRead(Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return authService.isStaffOrAdmin()
                ? (List<Report>) this.repository.findAll()
                : this.repository.findAllByUserId(auth.getUser().getId());
    }

    @Override
    public List<Report> findEntitiesByIdThatUserCanEdit(List<Long> ids, Auth auth) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return authService.isStaffOrAdmin()
                ? this.repository.findAllByIds(ids)
                : this.repository.findAllByIdsAndUserId(ids, auth.getUser().getId());
    }

    @Override
    public List<Report> findEntitiesThatUserCanReadExcludingIds(Auth auth, List<Long> ids) throws AuthNullException {
        if (auth == null) {
            throw new AuthNullException();
        }
        return authService.isStaffOrAdmin()
                ? this.repository.findAllExcludingIds(ids)
                : this.repository.findAllByUserIdExcludingIds(auth.getUser().getId(), ids);
    }

    @Override
    public WebSocketDestinationsEnum getWebSocketDestination() {
        return WebSocketDestinationsEnum.REPORT;
    }
}
