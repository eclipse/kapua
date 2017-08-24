/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.authorization.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.eclipse.kapua.app.console.module.api.shared.model.Enum;
import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

public class GwtPermission extends KapuaBaseModel {

    private static final long serialVersionUID = -7753268319786525424L;

    /**
     * Defines the actions allowed by the {@link GwtPermission}
     */
    public enum GwtAction implements IsSerializable, Enum {
        read, //
        write, //
        delete, //
        connect, //
        execute,

        ALL;

        @Override
        public String toString() {
            return this.name();
        }
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X get(String property) {
        if ("actionEnum".equals(property)) {
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
     * @param domainName
     *            The domain name of the permission
     * @param action
     *            The {@link GwtAction} of the permission
     * @param targetScopeId
     *            The target scope id of the permission
     * @param groupId
     *            The group id of the permission
     */
    public GwtPermission(String domainName, GwtAction action, String targetScopeId, String groupId, boolean forwardable) {
        this();
        setDomain(domainName);
        setAction(action != null ? action.name() : null);
        setTargetScopeId(targetScopeId);
        setGroupId(groupId);
        setForwardable(forwardable);
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

        if (getAction() != null) {
            sb.append(getDomain());
        } else {
            sb.append("*");
        }

        sb.append(":");
        if (getAction() != null) {
            sb.append(getActionEnum().name());
        } else {
            sb.append("*");
        }

        sb.append(":");
        if (getTargetScopeId() != null) {
            sb.append(getTargetScopeId());
        } else {
            sb.append("*");
        }

        sb.append(":");
        if (getGroupId() != null) {
            sb.append(getTargetScopeId());
        } else {
            sb.append("*");
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

    /**
     * @return the group id of this permission
     * @since 1.0.0
     */
    public String getGroupId() {
        return get("groupId");
    }

    public void setGroupId(String groupId) {
        set("groupId", groupId);
    }

    public boolean getForwardable() {
        return get("forwardable");
    }

    public void setForwardable(boolean forwardable) {
        set("forwardable", forwardable);
    }

}
