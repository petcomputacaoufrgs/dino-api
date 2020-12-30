package br.ufrgs.inf.pet.dinoapi.service.log_error;

import br.ufrgs.inf.pet.dinoapi.entity.log_error.LogAPIError;
import br.ufrgs.inf.pet.dinoapi.repository.log_error.LogApiErrorRepository;
import org.springframework.stereotype.Service;

@Service
public class LogAPIErrorServiceImpl implements LogAPIErrorService {
    private final LogApiErrorRepository repository;

    public LogAPIErrorServiceImpl(LogApiErrorRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(LogAPIError entity) {
        this.repository.save(entity);
    }
}
