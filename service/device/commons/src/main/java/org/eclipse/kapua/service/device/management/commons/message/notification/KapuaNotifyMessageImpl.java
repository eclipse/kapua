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
package org.eclipse.kapua.service.device.management.commons.message.notification;

import org.eclipse.kapua.message.internal.KapuaMessageImpl;
import org.eclipse.kapua.service.device.management.message.notification.KapuaNotifyChannel;
import org.eclipse.kapua.service.device.management.message.notification.KapuaNotifyMessage;
import org.eclipse.kapua.service.device.management.message.notification.KapuaNotifyPayload;

/**
 * {@link KapuaNotifyMessage} implementation.
 *
 * @since 1.0.0
 */
public class KapuaNotifyMessageImpl extends KapuaMessageImpl<KapuaNotifyChannel, KapuaNotifyPayload> implements KapuaNotifyMessage {

    private static final long serialVersionUID = 4475824062529778773L;
}
