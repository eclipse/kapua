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
package org.eclipse.kapua.service.device.management.message.notification;

import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Kapua notify message payload object definition.
 *
 * @since 1.0
 */
public interface KapuaNotifyPayload extends KapuaPayload {

    KapuaId getOperationId();

    void setOperationId(KapuaId operationId);

    OperationStatus getStatus();

    void setStatus(OperationStatus status);

    Integer getProgress();

    void setProgress(Integer progress);
}
