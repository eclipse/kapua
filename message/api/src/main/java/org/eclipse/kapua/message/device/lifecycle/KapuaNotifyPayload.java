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
package org.eclipse.kapua.message.device.lifecycle;

import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Kapua notify message payload object definition.
 *
 * @since 1.0
 */
public interface KapuaNotifyPayload extends KapuaPayload {

    public KapuaId getOperationId();

    public void setOperationId(KapuaId operationId);

    public String getOperationStatus();

    public void setOperationStatus(String operationStatus);

    public Integer getOperationProgress();

    public void setOperationProgress(Integer operationProgress);

    public String getErrorMessage();

    public void setErrorMessage(String errorMessage);
}
