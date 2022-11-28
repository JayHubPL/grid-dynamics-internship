package com.griddynamics;

import java.util.Optional;

/**
 * Password could be optionally adden, but in cryptographicly correct way.
 */
public class Credentials {
    private final Optional<String> username;
    private final boolean authenticated;

    private Credentials(String username, boolean authenticated) {
        this.username = Optional.ofNullable(username);
        this.authenticated = authenticated;
    }

    public static Credentials AUTH_GRANTED(String username) {
        return new Credentials(username, true);
    }

    public static Credentials AUTH_FAILED(String username) {
        return new Credentials(username, false);
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public Optional<String> getUsername() {
        return username;
    }

}
