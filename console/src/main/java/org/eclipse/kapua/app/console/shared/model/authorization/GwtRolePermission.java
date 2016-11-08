package org.eclipse.kapua.app.console.shared.model.authorization;

import org.eclipse.kapua.app.console.shared.model.GwtEntityModel;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtAction;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtDomain;

public class GwtRolePermission extends GwtEntityModel {

    private static final long serialVersionUID = 6331197556606146242L;

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X get(String property) {
        if ("domainEnum".equals(property)) {
            return (X) (GwtDomain.valueOf(getDomain()));
        } else if ("actionEnum".equals(property)) {
            return (X) (GwtAction.valueOf(getAction()));
        } else {
            return super.get(property);
        }
    }

    public GwtRolePermission() {
        super();
    }

    public String getRoleId() {
        return get("roleId");
    }

    public void setRoleId(String roleId) {
        set("roleId", roleId);
    }

    /**
     * @return the domain of this permission
     * @since 1.0.0
     */
    public String getDomain() {
        return get("domain");
    }

    public GwtDomain getDomainEnum() {
        return get("domainEnum");
    }

    public void setDomain(String domain) {
        set("domain", domain);
    }

    /**
     * @return the action of this permission
     * @since 1.0.0
     */
    public String getAction() {
        return get("action");
    }

    public GwtAction getActionEnum() {
        return get("actionEnum");
    }

    public void setAction(String action) {
        set("action", action);
    }

    /**
     * @return the target scope id of this permission
     * @since 1.0.0
     */
    public String getTargetScopeId() {
        return get("targetScopeId");
    }

    public void setTargetScopeId(String targetScopeId) {
        set("targetScopeId", targetScopeId);
    }
}