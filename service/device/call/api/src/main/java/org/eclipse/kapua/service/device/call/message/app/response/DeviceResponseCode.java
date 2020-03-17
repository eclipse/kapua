/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.app.response;


import org.eclipse.kapua.service.device.call.message.app.request.DeviceRequestMessage;

/**
 * {@link DeviceResponseCode} definition.
 * <p>
 * REST-like response codes.
 *
 * @since 1.0.0
 */
public interface DeviceResponseCode {

    /**
     * The {@link DeviceRequestMessage} has been successfully processed.
     *
     * @return {@code truw} if the {@link DeviceRequestMessage} has been successfully processed, {@code false} otherwise.
     * @since 1.0.0
     */
    boolean isAccepted();

    /**
     * The {@link DeviceRequestMessage} is malformed.
     *
     * @return {@code truw} if the {@link DeviceRequestMessage} is malformed, {@code false} otherwise.
     * @since 1.0.0
     */
    boolean isBadRequest();

    /**
     * The {@link DeviceRequestMessage} resource is not available.
     *
     * @return {@code truw} if the {@link DeviceRequestMessage}  resource is not available, {@code false} otherwise.
     * @since 1.0.0
     */
    boolean isNotFound();

    /**
     * The {@link DeviceRequestMessage} has encountered an error during processing.
     *
     * @return {@code truw} if the {@link DeviceRequestMessage} has encountered an error during processing, {@code false} otherwise.
     * @since 1.0.0
     */
    boolean isInternalError();
}
