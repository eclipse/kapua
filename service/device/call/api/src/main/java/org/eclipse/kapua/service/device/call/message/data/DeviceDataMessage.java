/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.call.message.data;

import org.eclipse.kapua.service.device.call.message.DeviceMessage;

/**
 * {@link DeviceDataMessage} definition.
 *
 * @param <C> The {@link DeviceDataChannel} type.
 * @param <P> The {@link DeviceDataPayload} type.
 * @since 1.0.0
 */
public interface DeviceDataMessage<C extends DeviceDataChannel, P extends DeviceDataPayload> extends DeviceMessage<C, P> {

}
