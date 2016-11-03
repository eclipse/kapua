package org.eclipse.kapua.app.console.shared.model.device.management.bundles;

import org.eclipse.kapua.app.console.client.messages.ValidationMessages;
import org.eclipse.kapua.app.console.shared.model.KapuaBaseModel;

import com.google.gwt.core.client.GWT;

public class GwtBundle extends KapuaBaseModel {

    private static final long serialVersionUID = -5494700079001271509L;

    @Override
    @SuppressWarnings("unchecked")
    public <X> X get(String property) {
        ValidationMessages MSGS = GWT.create(ValidationMessages.class);
        if ("statusLoc".equals(property)) {
            return (X) MSGS.getString(getStatus());
        }
        return super.get(property);
    }

    public void setId(String id) {
        set("id", id);
    }

    public String getId() {
        return get("id");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getName() {
        return get("name");
    }

    public void setStatus(String status) {
        set("status", status);
    }

    public String getStatus() {
        return get("status");
    }

    public void setVersion(String version) {
        set("version", version);
    }

    public String getVersion() {
        return get("version");
    }
}
