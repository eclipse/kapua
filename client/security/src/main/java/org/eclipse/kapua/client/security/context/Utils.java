/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.model.id.KapuaId;

public class Utils {

    // full client id, with account prepended
    protected static final String FULL_CLIENT_ID_PATTERN = "%s|%s";

    private Utils() {
    }

    public static String getFullClientId(KapuaId scopeId, String clientId) {
        return String.format(FULL_CLIENT_ID_PATTERN, scopeId.toCompactId(), clientId);
    }

    public static String getFullClientId(SessionContext sessionContext) {
        return getFullClientId(sessionContext.getScopeId(), sessionContext.getClientId());
    }

}
