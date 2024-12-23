package org.punna.commons.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EntityNotFoundException extends EventApplicationException {
    public EntityNotFoundException(String modelName, String id) {
        super(String.format("%s with id::%s not found", modelName, id));
    }
}