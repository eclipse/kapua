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
     * Defines the domain of the object protected by the Permission
     */
    public enum Domain {
        account, user, data, device, rule, apis, broker, system, simcard, simprovider, certificate, self;
    }

    /**
     * Defines the actions allowed by the Permission
     */
    public enum Action {
        view, manage, connect, all;
    }

    private Domain m_domain;
    private Action m_action;
    private long m_accountId;

    /**
     * Gwt Permission constructor.
     * 
     * @param domain
     * @param action
     * @param accountId
     */
    public GwtPermission(Domain domain, Action action, long accountId) {
        m_domain = domain;
        m_action = action;
        m_accountId = accountId;
    }

    /**
     * Gwt Permission constructor.
     * 
     * @param domain
     * @param action
     */
    public GwtPermission(Domain domain, Action action) {
        m_domain = domain;
        m_action = action;
        m_accountId = -1;
    }

    /**
     * Gwt Permission constructor.
     * 
     * @param domain
     */
    public GwtPermission(Domain domain) {
        m_domain = domain;
        m_action = null;
        m_accountId = -1;
    }

    /**
     * Returns the string representation for this Permission
     */
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(m_domain.name());
        if (m_action != null) {
            sb.append(":")
                    .append(m_action.name());
        }
        if (m_accountId >= 0) {
            sb.append(":")
                    .append(String.valueOf(m_accountId));
        }
        return sb.toString();
    }

    /**
     * @return the domain
     */
    public Domain getDomain() {
        return m_domain;
    }

    /**
     * @return the action
     */
    public Action getAction() {
        return m_action;
    }

    /**
     * @return the accountId
     */
    public long getAccountId() {
        return m_accountId;
    }
}
