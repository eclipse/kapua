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
package org.eclipse.kapua.broker.artemis.plugin.security.context;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

import org.eclipse.kapua.client.security.ServiceClient.SecurityAction;
import org.eclipse.kapua.commons.util.KapuaDateUtils;

public class ConnectionToken {

    private SecurityAction action;
    private Instant actionTime;

    public ConnectionToken(SecurityAction action, Instant actionTime) {
        this.action = action;
        this.actionTime = actionTime;
    }

    public SecurityAction getAction() {
        return action;
    }

    public String getActionDate() {
        try {
            return KapuaDateUtils.formatDate(Date.from(actionTime));
        } catch (ParseException e) {
            return "N/A";
        }
    }

}
