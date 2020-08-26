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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.message.internal.device.lifecycle;

import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectPayload;

/**
 * {@link KapuaDisconnectMessage} implementation.
 *
 * @since 1.0.0
 */
public class KapuaDisconnectMessageImpl extends AbstractLifecycleMessageImpl<KapuaDisconnectChannel, KapuaDisconnectPayload> implements KapuaDisconnectMessage {

    private static final long serialVersionUID = 1L;
}
