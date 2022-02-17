/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.call.message.kura.lifecycle;

import org.eclipse.kapua.service.device.call.kura.Kura;
import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;
import org.eclipse.kapua.service.device.call.message.lifecycle.DeviceLifecyclePayload;

/**
 * {@code abstract} base class for {@link Kura} {@link DeviceLifecyclePayload}
 *
 * @since 1.2.0
 */
public class AbstractKuraLifecyclePayload extends KuraPayload implements DeviceLifecyclePayload {
}
