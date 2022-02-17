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
package org.eclipse.kapua.service.device.call.message.app.response;

import org.eclipse.kapua.service.device.call.message.app.DeviceAppMessage;

/**
 * {@link DeviceResponseMessage} definition.
 *
 * @param <C> The {@link DeviceResponseChannel} type.
 * @param <P> The {@link DeviceResponsePayload} type.
 * @since 1.0.0
 */
public interface DeviceResponseMessage<C extends DeviceResponseChannel, P extends DeviceResponsePayload> extends DeviceAppMessage<C, P> {

}
