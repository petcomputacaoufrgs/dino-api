package br.ufrgs.inf.pet.dinoapi.task.auth;

public interface ResponsibleAuthTask {
    /**
     * Delete old recover passwords attempts without conclusion
     * @throws InterruptedException
     */
    void deleteOldData() throws InterruptedException;
}
