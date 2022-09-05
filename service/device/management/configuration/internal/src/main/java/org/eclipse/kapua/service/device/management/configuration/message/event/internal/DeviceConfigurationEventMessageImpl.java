/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.configuration.message.event.internal;

import org.eclipse.kapua.message.internal.KapuaMessageImpl;
import org.eclipse.kapua.service.device.management.configuration.message.event.DeviceConfigurationEventChannel;
import org.eclipse.kapua.service.device.management.configuration.message.event.DeviceConfigurationEventMessage;
import org.eclipse.kapua.service.device.management.configuration.message.event.DeviceConfigurationEventPayload;

/**
 * {@link DeviceConfigurationEventMessage} implementation
 *
 * @since 2.0.0
 */
public class DeviceConfigurationEventMessageImpl extends KapuaMessageImpl<DeviceConfigurationEventChannel, DeviceConfigurationEventPayload>
        implements DeviceConfigurationEventMessage<DeviceConfigurationEventChannel, DeviceConfigurationEventPayload> {
}
