/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.message.internal.device.lifecycle;

import org.eclipse.kapua.message.device.lifecycle.KapuaLifecycleChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaLifecycleMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaLifecyclePayload;
import org.eclipse.kapua.message.internal.KapuaMessageImpl;

/**
 * {@link KapuaLifecycleMessage} {@code abstract} implementation.
 *
 * @since 1.1.0
 */
public abstract class AbstractLifecycleMessageImpl<C extends KapuaLifecycleChannel, P extends KapuaLifecyclePayload> extends KapuaMessageImpl<C, P> implements KapuaLifecycleMessage<C, P> {

}
