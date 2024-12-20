package org.punna.commons.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class EventApplicationException extends RuntimeException {

    private final Map<String, String> errors = new HashMap<String, String>();

    public EventApplicationException(String message) {
        super(message);
    }
}
