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
package org.eclipse.kapua.service.device.management.commons.message.event;

import org.eclipse.kapua.message.internal.KapuaMessageImpl;
import org.eclipse.kapua.service.device.management.message.event.KapuaManagementEventChannel;
import org.eclipse.kapua.service.device.management.message.event.KapuaManagementEventMessage;
import org.eclipse.kapua.service.device.management.message.event.KapuaManagementEventPayload;

/**
 * {@link KapuaManagementEventMessage} implementation.
 *
 * @since 2.0.0
 */
public class KapuaEventMessageImpl<C extends KapuaManagementEventChannel, P extends KapuaManagementEventPayload> extends KapuaMessageImpl<C, P> implements KapuaManagementEventMessage<C, P> {

    private static final long serialVersionUID = 4475824062529778773L;
}
