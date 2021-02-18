package br.ufrgs.inf.pet.dinoapi.service.log_error;

import br.ufrgs.inf.pet.dinoapi.configuration.properties.LogConfig;
import br.ufrgs.inf.pet.dinoapi.entity.log_error.LogAPIError;
import br.ufrgs.inf.pet.dinoapi.repository.log_error.LogApiErrorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class LogAPIErrorServiceImpl implements LogAPIErrorService {
    private final LogApiErrorRepository logApiErrorRepository;

    @Autowired
    public LogAPIErrorServiceImpl(LogApiErrorRepository repository) {
        this.logApiErrorRepository = repository;
    }

    @Override
    public void save(LogAPIError entity) {
        this.logApiErrorRepository.save(entity);
    }

    @Override
    public void deleteOldItems(LocalDateTime deadline) {
        logApiErrorRepository.deleteAllByDate(deadline);
    }
}
