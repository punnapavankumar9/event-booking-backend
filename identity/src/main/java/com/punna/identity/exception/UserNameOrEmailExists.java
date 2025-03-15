package com.punna.identity.exception;

import com.punna.commons.exception.EventApplicationException;

public class UserNameOrEmailExists extends EventApplicationException {
    public UserNameOrEmailExists(String usernameOrEmail, boolean isUsername) {
        super("user with " + (isUsername ? "username " : "email ") + usernameOrEmail + " already exists");
    }
}
