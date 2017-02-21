package org.eclipse.kapua.app.console.client.group;

import org.eclipse.kapua.app.console.shared.model.query.GwtQuery;

public class GwtGroupQuery extends GwtQuery{

    /**
     * 
     */
    private static final long serialVersionUID = 416159571099764329L;
    
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}