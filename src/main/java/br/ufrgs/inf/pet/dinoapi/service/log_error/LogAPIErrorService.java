package br.ufrgs.inf.pet.dinoapi.service.log_error;

import br.ufrgs.inf.pet.dinoapi.entity.log_error.LogAPIError;

public interface LogAPIErrorService {
    void save(LogAPIError entity);
}
