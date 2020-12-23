package br.ufrgs.inf.pet.dinoapi.configuration.gson.gson_exclude_strategy;
import br.ufrgs.inf.pet.dinoapi.websocket.model.SynchronizableWSUpdateModel;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class SynchronizableWSExcludeStrategy implements ExclusionStrategy {
    public boolean shouldSkipField(FieldAttributes f) {
        return (f.getDeclaringClass() == SynchronizableWSUpdateModel.class && f.getName().equals("localId"));
    }

    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}