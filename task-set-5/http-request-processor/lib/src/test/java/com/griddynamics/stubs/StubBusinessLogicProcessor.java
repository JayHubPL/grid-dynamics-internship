package com.griddynamics.stubs;

import java.net.http.HttpResponse;

import com.griddynamics.CustomHttpRequest;
import com.griddynamics.CustomHttpResponse;
import com.griddynamics.interfaces.BusinessLogicProcessor;

public class StubBusinessLogicProcessor implements BusinessLogicProcessor {

    @Override
    public HttpResponse<String> applyLogic(CustomHttpRequest request) {
        return new CustomHttpResponse(request);
    }
    
}
