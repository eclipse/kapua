/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.client.security.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthAcl {

    public enum Action {
        all,
        read,
        write,
        admin,
        readAdmin,
        writeAdmin
    }

    @JsonProperty("match")
    private String match;

    @JsonProperty("action")
    private Action action;

    public AuthAcl() {
    }

    public AuthAcl(String match, Action action) {
        this.match = match;
        this.action = action;
    }

    public String getMatch() {
        return match;
    }

    public Action getAction() {
        return action;
    }

    @Override
    public String toString() {
        return match + ":" + action;
    }
}
