package com.punna.identity.exception;

import org.punna.commons.exception.EventApplicationException;

public class InvalidUsernamePasswordCombination extends EventApplicationException {
    public InvalidUsernamePasswordCombination(String username) {
        super("Invalid username or password for username: " + username);
    }
}
