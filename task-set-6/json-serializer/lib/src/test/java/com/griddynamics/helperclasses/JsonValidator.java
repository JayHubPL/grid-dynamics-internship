package com.griddynamics.helperclasses;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonValidator {
    
    public boolean isValid(String json) {
        try {
            new JSONObject(json);
        } catch (JSONException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

}
