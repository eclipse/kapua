package org.eclipse.kapua.app.console.shared.model;

import java.io.Serializable;


public class GwtChannel extends GwtUpdatableEntityModel implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 6939874517542354331L;
    
    public String getName(){
        return get("name"); 
        }
    
    public void setName(String name){
        set("name", name);
    }
    
    public String getRead(){
        return get("read");
    }
    
    public void setRead(String read){
        set("read", read);
    }
    
    public String getAction(){
        return get("action");
    }
    
    public void setAction(String action){
        set("action", action);
    }
    
    public String getValue(){
        return get("value");
    }
    
    public void setValue(String value){
        set("value", value);
    }
    
    
}
