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
package org.eclipse.kapua.qa.common;

import java.util.HashMap;
import java.util.Map;

import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class SimulatedDevice {

    private String accountName;

    private String clientId;

    private Map<String, SimulatedDeviceApplication> mockApplications = new HashMap<>();

    private boolean started;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Map<String, SimulatedDeviceApplication> getMockApplications() {
        return mockApplications;
    }

    public void setMockApplications(Map<String, SimulatedDeviceApplication> mockApplications) {
        this.mockApplications = mockApplications;
    }

    public void started() {
        this.started = true;
    }

    public void stopped() {
        this.started = false;
    }

    public boolean isStarted() {
        return started;
    }
}
