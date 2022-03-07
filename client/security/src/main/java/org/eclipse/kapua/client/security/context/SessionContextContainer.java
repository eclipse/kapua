/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.client.security.context;

public class SessionContextContainer {

    private SessionContext current;
    private SessionContext old;

    public SessionContextContainer(SessionContext current, SessionContext old) {
        this.current = current;
        this.old = old;
    }

    public SessionContext getCurrent() {
        return current;
    }

    public SessionContext getOld() {
        return old;
    }

    public String getOldConnectionId() {
        return old != null ? old.getConnectionId() : null;
    }

}
