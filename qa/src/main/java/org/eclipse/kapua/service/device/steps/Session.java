/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.steps;

import org.eclipse.kapua.commons.util.ThrowingRunnable;
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
