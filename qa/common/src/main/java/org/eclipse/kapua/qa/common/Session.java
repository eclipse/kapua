/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.qa.common;

import org.eclipse.kapua.commons.util.ThrowingRunnable;
import org.eclipse.kapua.qa.common.With;
import org.eclipse.kapua.service.authentication.LoginCredentials;

import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class Session {

    private LoginCredentials credentials;

    public void setCredentials(LoginCredentials credentials) {
        this.credentials = credentials;
    }

    public LoginCredentials getCredentials() {
        return credentials;
    }

    public void withLogin(ThrowingRunnable runnable) throws Exception {
        With.withLogin(credentials, runnable);
    }

}
