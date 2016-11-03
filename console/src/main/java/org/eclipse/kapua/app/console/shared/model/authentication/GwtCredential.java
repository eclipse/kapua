package org.eclipse.kapua.app.console.shared.model.authentication;

import org.eclipse.kapua.app.console.shared.model.GwtEntityModel;

public class GwtCredential extends GwtEntityModel {

    private static final long serialVersionUID = -469650746033310482L;

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X get(String property) {
        if ("credentialTypeEnum".equals(property)) {
            return (X) GwtCredentialType.valueOf(getCredentialType());
        } else {
            return super.get(property);
        }
    }

    public GwtCredential() {
        super();
    }

    public String getUserId() {
        return get("userId");
    }

    public void setUserId(String userId) {
        set("userId", userId);
    }

    public String getCredentialType() {
        return get("credentialType");
    }

    public GwtCredentialType getCredentialTypeEnum() {
        return get("credentialTypeEnum");
    }

    public void setCredentialType(String credentialType) {
        set("credentialType", credentialType);
    }

    public String getCredentialKey() {
        return get("credentialKey");
    }

    public void setCredentialKey(String credentialKey) {
        set("credentialKey", credentialKey);
    }

}