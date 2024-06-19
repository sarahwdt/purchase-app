package org.leroyjenkins.paymenttestapp.exception;

import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

@Getter
public class EntityNotFoundException extends BusinessLogicException {
    private static final String ERROR_MESSAGE = "Entity not found";
    private final String entityName;
    private final Serializable entityId;

    public EntityNotFoundException(String entityName, Serializable entityId) {
        super(ERROR_MESSAGE, String.format("'%s' with id '%s': '%s'", entityName, entityId, ERROR_MESSAGE),
                Map.of("entity", entityName, "id", entityId));
        this.entityName = entityName;
        this.entityId = entityId;
    }
}
