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
