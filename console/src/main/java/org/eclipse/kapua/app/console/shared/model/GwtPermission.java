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

import java.io.Serializable;

public class GwtPermission extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = -7753268319786525424L;

    /**
     * Defines the domain of the object protected by the {@link GwtPermission}
     */
    public enum GwtDomain {
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
    public enum GwtAction {
        read, //
        write, //
        delete, //
        connect, //
        exec;
    }

    private GwtDomain domain;
    private GwtAction action;
    private String targetScopeId;

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
        this.domain = domain;
        this.action = action;
        this.targetScopeId = targetScopeId;
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
        sb.append(domain.name());

        if (action != null) {
            sb.append(":")
                    .append(action.name());
        }
        if (targetScopeId != null) {
            sb.append(":")
                    .append(targetScopeId);
        }
        return sb.toString();
    }

    /**
     * @return the domain of this permission
     * @since 1.0.0
     */
    public GwtDomain getDomain() {
        return domain;
    }

    /**
     * @return the action of this permission
     * @since 1.0.0
     */
    public GwtAction getAction() {
        return action;
    }

    /**
     * @return the target scope id of this permission
     * @since 1.0.0
     */
    public String getTargetScopeId() {
        return targetScopeId;
    }
}