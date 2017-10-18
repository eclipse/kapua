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
 *******************************************************************************/
package org.eclipse.kapua.message.internal.device.management;

import org.eclipse.kapua.message.device.lifecycle.KapuaNotifyPayload;
import org.eclipse.kapua.message.internal.KapuaPayloadImpl;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Kapua notify message payload object reference implementation.
 *
 * @since 1.0
 */
public class KapuaNotifyPayloadImpl extends KapuaPayloadImpl implements KapuaNotifyPayload {

    private KapuaId operationId;
    private String operationStatus;
    private Integer operationProgress;
    private String errorMessage;

    @Override
    public KapuaId getOperationId() {
        return operationId;
    }

    @Override
    public void setOperationId(KapuaId operationId) {
        this.operationId = operationId;
    }

    @Override
    public String getOperationStatus() {
        return operationStatus;
    }

    @Override
    public void setOperationStatus(String operationStatus) {
        this.operationStatus = operationStatus;
    }

    @Override
    public Integer getOperationProgress() {
        return operationProgress;
    }

    @Override
    public void setOperationProgress(Integer operationProgress) {
        this.operationProgress = operationProgress;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
