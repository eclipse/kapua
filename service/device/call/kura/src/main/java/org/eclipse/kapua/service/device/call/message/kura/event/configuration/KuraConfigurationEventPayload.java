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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.kura.event.configuration;

import org.eclipse.kapua.service.device.call.kura.Kura;
import org.eclipse.kapua.service.device.call.kura.model.configuration.KuraDeviceConfiguration;
import org.eclipse.kapua.service.device.call.message.app.event.DeviceManagementEventPayload;
import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;

/**
 * {@link KuraDeviceConfiguration} {@link DeviceManagementEventPayload} {@link Kura} implementation.
 *
 * @since 2.0.0
 */
public class KuraConfigurationEventPayload extends KuraPayload implements DeviceManagementEventPayload {
}
