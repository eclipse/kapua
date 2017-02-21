package org.eclipse.kapua.app.console.client.group;

import org.eclipse.kapua.app.console.shared.model.GwtEntityCreator;

public class GwtGroupCreator extends GwtEntityCreator{

    /**
     * 
     */
    private static final long serialVersionUID = -2831647463538674359L;
    
    private String name;

    public GwtGroupCreator(){
        super();
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}