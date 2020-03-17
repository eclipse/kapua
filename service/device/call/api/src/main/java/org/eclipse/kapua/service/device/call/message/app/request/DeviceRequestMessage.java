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
package org.eclipse.kapua.service.device.call.message.app.request;

import org.eclipse.kapua.service.device.call.message.app.DeviceAppMessage;

/**
 * {@link DeviceRequestMessage} definition.
 *
 * @param <D> The {@link DeviceRequestChannel} type.
 * @param <P> The {@link DeviceRequestPayload} type.
 * @since 1.0.0
 */
public interface DeviceRequestMessage<D extends DeviceRequestChannel, P extends DeviceRequestPayload> extends DeviceAppMessage<D, P> {

}
