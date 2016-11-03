package org.eclipse.kapua.app.console.shared.model.query;

import java.io.Serializable;

public class GwtQuery implements Serializable {

    private static final long serialVersionUID = 3080860571269787362L;

    private String scopeId;

    public GwtQuery() {
        super();
    }

    public String getScopeId() {
        return scopeId;
    }

    public void setScopeId(String scopeId) {
        this.scopeId = scopeId;
    }
}