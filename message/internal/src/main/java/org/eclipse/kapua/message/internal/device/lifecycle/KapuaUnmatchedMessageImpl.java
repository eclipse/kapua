/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.message.device.lifecycle.KapuaUnmatchedChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaUnmatchedMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaUnmatchedPayload;
import org.eclipse.kapua.message.internal.KapuaMessageImpl;

/**
 * Kapua unmatched message object reference implementation.
 */
public class KapuaUnmatchedMessageImpl extends KapuaMessageImpl<KapuaUnmatchedChannel, KapuaUnmatchedPayload> implements KapuaUnmatchedMessage {

    private static final long serialVersionUID = 1L;

}
