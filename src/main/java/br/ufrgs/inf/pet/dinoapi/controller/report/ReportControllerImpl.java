package br.ufrgs.inf.pet.dinoapi.controller.report;

import br.ufrgs.inf.pet.dinoapi.controller.synchronizable.SynchronizableControllerImpl;
import br.ufrgs.inf.pet.dinoapi.entity.report.Report;
import br.ufrgs.inf.pet.dinoapi.model.report.ReportDataModel;
import br.ufrgs.inf.pet.dinoapi.repository.report.ReportRepository;
import br.ufrgs.inf.pet.dinoapi.service.report.ReportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.ufrgs.inf.pet.dinoapi.constants.PathConstants.REPORT;

@RestController
@RequestMapping(REPORT)
public class ReportControllerImpl extends SynchronizableControllerImpl<
        Report, Long, ReportDataModel, ReportRepository, ReportServiceImpl> {
    @Autowired
    protected ReportControllerImpl(ReportServiceImpl service) {
        super(service);
    }
}