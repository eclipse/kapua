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
package org.eclipse.kapua.service.device.management.request;

import org.eclipse.kapua.message.KapuaPayload;

/**
 * Kapua request message payload definition.<br>
 * This object defines the payload for a Kapua request message.<br>
 * The request message is used to perform interactive operations with the device (e.g. to send command to the device, to ask configurations...)
 * 
 * @since 1.0
 *
 */
public interface KapuaRequestPayload extends KapuaPayload {

}
