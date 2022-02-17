/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.message.notification;

import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Notify {@link KapuaPayload} definition.
 *
 * @since 1.0.0
 */
public interface KapuaNotifyPayload extends KapuaPayload {

    /**
     * Gets the operation {@link KapuaId}.
     *
     * @return The operation {@link KapuaId}.
     * @since 1.0.0
     */
    KapuaId getOperationId();

    /**
     * Sets the operation {@link KapuaId}.
     *
     * @param operationId The operation {@link KapuaId}.
     * @since 1.0.0
     */
    void setOperationId(KapuaId operationId);

    /**
     * Gets the resource.
     *
     * @return The resource.
     * @since 1.0.0
     * @deprecated Since 1.2.0. Please make use of {@link KapuaNotifyChannel#getResources()}
     */
    @Deprecated
    String getResource();

    /**
     * Sets the resource.
     *
     * @param resource The resource.
     * @since 1.0.0
     * @deprecated Since 1.2.0. Please make use of {@link KapuaNotifyChannel#setResources(String[])}
     */
    @Deprecated
    void setResource(String resource);

    /**
     * Gets the {@link NotifyStatus}.
     *
     * @return The {@link NotifyStatus}.
     * @since 1.0.0
     */
    NotifyStatus getStatus();

    /**
     * Sets the {@link NotifyStatus}.
     *
     * @param status The {@link NotifyStatus}.
     * @since 1.0.0
     */
    void setStatus(NotifyStatus status);

    /**
     * Gets the operation progress.
     *
     * @return The operation progress.
     * @since 1.0.0
     */
    Integer getProgress();

    /**
     * Sets the operation progress.
     *
     * @param progress The operation progress.
     * @since 1.0.0
     */
    void setProgress(Integer progress);

    /**
     * Gets the message.
     *
     * @return The message.
     * @since 1.2.0
     */
    String getMessage();

    /**
     * Gets the message.
     *
     * @param message The message.
     * @since 1.2.0
     */
    void setMessage(String message);
}
