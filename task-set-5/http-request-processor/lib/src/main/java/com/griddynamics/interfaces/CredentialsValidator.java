package com.griddynamics.interfaces;

import com.griddynamics.Credentials;

public interface CredentialsValidator {
    
    /**
     * Checks if credentials passed as an argument are valid and grant positive authentication.
     * @param decodedCredentials decoded credentials to be checked
     * @return {@code Credentials} instance containing info on the athentication status and if
     * it was successful, it also contains user's name and password
     */
    Credentials checkCredentials(String decodedCredentials);

}
