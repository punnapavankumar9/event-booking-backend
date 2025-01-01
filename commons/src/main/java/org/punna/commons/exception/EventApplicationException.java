package org.punna.commons.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class EventApplicationException extends RuntimeException {

    private final Map<String, String> errors = new HashMap<>();

    private Integer status = 500;

    public EventApplicationException(String message) {
        super(message);
    }

    public EventApplicationException(String message, Integer status) {
        this(message);
        this.status = status;
    }

}
