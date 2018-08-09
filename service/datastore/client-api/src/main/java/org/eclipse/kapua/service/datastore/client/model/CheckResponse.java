/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.client.model;

public class CheckResponse {

    public enum ESHealthStatus {
        GREEN,
        YELLOW,
        RED
    }

    private ESHealthStatus status;

    public CheckResponse(ESHealthStatus status) {
        this.status = status;
    }

    public ESHealthStatus getStatus() {
        return status;
    }

    public boolean isHealthy() {
        return !ESHealthStatus.RED.equals(status);
    }

}
