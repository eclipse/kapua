package org.eclipse.kapua.app.console.shared.model;

public class GwtAccessRole extends GwtUpdatableEntityModel{
    /**
     * 
     */
    private static final long serialVersionUID = 7272103538588644277L;
    
    public String getAccessInfoId(){
        return (String) get ("accessInfoId");
    }
    
    public void setAccessInfoId(String accessInfoId){
        set ("accessInfoId", accessInfoId);
    }
    
    public String getRoleId(){
        return (String) get ("roleId");
    }
    public void setRoleId(String roleId){
        set("roleId", roleId);
    }
    
    public String getType()
    {return (String) get ("type");
    }
    
    public void setType(String type){
        set ("type", type);
    }
}
