package org.eclipse.kapua.app.console.shared.model;

import java.io.Serializable;

public class GwtEntityCreator implements Serializable {

    private static final long serialVersionUID = -1755052755088203343L;

    private String scopeId;

    public GwtEntityCreator() {
        super();
    }

    public String getScopeId() {
        return scopeId;
    }

    public void setScopeId(String scopeId) {
        this.scopeId = scopeId;
    }
}