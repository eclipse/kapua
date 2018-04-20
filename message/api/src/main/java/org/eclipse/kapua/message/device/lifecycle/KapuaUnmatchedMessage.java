/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.message.device.lifecycle;

import org.eclipse.kapua.message.KapuaMessage;

/**
 * Kapua unmatched message object definition.<br>
 * This message is used, as last resource, if no control message filter matches the incoming topic.
 * 
 * @since 1.0
 *
 */
public interface KapuaUnmatchedMessage extends KapuaMessage<KapuaUnmatchedChannel, KapuaUnmatchedPayload> {

}
