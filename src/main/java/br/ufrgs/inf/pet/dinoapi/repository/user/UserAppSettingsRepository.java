package br.ufrgs.inf.pet.dinoapi.repository.user;

import br.ufrgs.inf.pet.dinoapi.entity.user.UserAppSettings;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório da entidade: {@link UserAppSettings}
 *
 * @author joao.silva
 */
@Repository
public interface UserAppSettingsRepository extends CrudRepository<UserAppSettings, Long>  {}
