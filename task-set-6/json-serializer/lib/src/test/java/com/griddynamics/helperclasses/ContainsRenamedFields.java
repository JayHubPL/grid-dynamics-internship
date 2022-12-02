package com.griddynamics.helperclasses;

import com.griddynamics.annotations.JsonAttribute;
import com.griddynamics.annotations.JsonSerializable;

@JsonSerializable
public class ContainsRenamedFields {
    
    @JsonAttribute(jsonFieldName = "renamed")
    private Object oldName;
    @JsonAttribute
    private Object dontRenameMe;

}
