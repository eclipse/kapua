package org.eclipse.kapua.app.console.shared.model.role;

import org.eclipse.kapua.app.console.shared.model.query.GwtQuery;

public class GwtRoleQuery extends GwtQuery {

    private static final long serialVersionUID = -5198696327167110220L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}