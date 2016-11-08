/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GwtPermission extends KapuaBaseModel {

    private static final long serialVersionUID = -7753268319786525424L;

    /**
     * Defines the domain of the object protected by the {@link GwtPermission}
     */
    public enum GwtDomain implements IsSerializable, Enum {
        account, //
        credential, //
        datastore, //
        device_connection, //
        device, //
        device_event, //
        device_management, //
        role, //
        user, //
        user_permission, //
    }

    /**
     * Defines the actions allowed by the {@link GwtPermission}
     */
    public enum GwtAction implements IsSerializable, Enum {
        read, //
        write, //
        delete, //
        connect, //
        exec;

        @Override
        public String toString() {
            return this.name();
        }
    }

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

    /**
     * Gwt Permission constructor.
     */
    public GwtPermission() {
        super();
    }

    /**
     * Gwt Permission constructor.
     * 
     * @param domain
     *            The {@link GwtDomain} of the permission
     * @param action
     *            The {@link GwtAction} of the permission
     * @param targetScopeId
     *            The target scope id of the permission
     */
    public GwtPermission(GwtDomain domain, GwtAction action, String targetScopeId) {
        this();
        setDomain(domain != null ? domain.name() : null);
        setAction(action != null ? action.name() : null);
        setTargetScopeId(targetScopeId);
    }

    /**
     * Returns the string representation for this {@link GwtPermission} in the following format:
     * <p>
     * {domain}[:{action}[:{targetScopeId]]
     * </p>
     * 
     * @return the formatted string representation of this {@link GwtPermission}
     * @since 1.0.0
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(getDomainEnum().name());

        if (getAction() != null) {
            sb.append(":")
                    .append(getActionEnum().name());
        }
        if (getTargetScopeId() != null) {
            sb.append(":")
                    .append(getTargetScopeId());
        }
        return sb.toString();
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