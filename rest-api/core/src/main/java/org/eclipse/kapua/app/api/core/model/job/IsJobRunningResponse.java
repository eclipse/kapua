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
package org.eclipse.kapua.app.api.core.model.job;

import javax.xml.bind.annotation.XmlElement;

public class IsJobRunningResponse {

    private boolean isRunning;

    public IsJobRunningResponse() {
    }

    public IsJobRunningResponse(boolean isRunning) {
        this.isRunning = isRunning;
    }

    @XmlElement(name = "isRunning")
    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

}
