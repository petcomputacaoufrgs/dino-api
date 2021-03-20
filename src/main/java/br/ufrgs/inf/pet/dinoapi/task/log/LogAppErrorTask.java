package br.ufrgs.inf.pet.dinoapi.task.log;

import br.ufrgs.inf.pet.dinoapi.configuration.properties.LogConfig;

public interface LogAppErrorTask {
    /**
     * Delete old log data in database using delay declared in {@link LogConfig}
     * @throws InterruptedException
     */
    void deleteOldData() throws InterruptedException;
}
