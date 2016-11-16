package org.eclipse.kapua.service.authentication.shiro;

import org.apache.shiro.authc.AuthenticationToken;
import org.eclipse.kapua.service.authentication.ApiKeyCredentials;

public class ApiKeyCredentialsImpl implements ApiKeyCredentials, AuthenticationToken {

    private static final long serialVersionUID = -5920944517814926028L;

    private String apiKey;

    public ApiKeyCredentialsImpl(String apiKey) {
        setApiKey(apiKey);
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }

    @Override
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Object getPrincipal() {
        return getApiKey();
    }

    @Override
    public Object getCredentials() {
        return getApiKey();
    }

}
