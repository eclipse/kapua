package org.eclipse.kapua.app.console.shared.model.authentication;

import org.eclipse.kapua.app.console.shared.model.KapuaBaseModel;

public class GwtLoginCredential extends KapuaBaseModel {

    private static final long serialVersionUID = -7683275209937145099L;

    public GwtLoginCredential() {
        super();
    }

    public GwtLoginCredential(String username, String password) {
        this();
        setUsername(username);
        setPassword(password);
    }

    public String getUsername() {
        return get("username");
    }

    public void setUsername(String username) {
        set("username", username);
    }

    public String getPassword() {
        return get("password");
    }

    public void setPassword(String password) {
        set("password", password);
    }
}