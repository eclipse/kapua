/*******************************************************************************
 * Copyright (c) 2019, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
