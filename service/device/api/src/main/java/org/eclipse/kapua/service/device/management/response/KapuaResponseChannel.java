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
package org.eclipse.kapua.service.device.management.response;

import org.eclipse.kapua.service.device.management.KapuaAppChannel;

/**
 * Kapua response message channel definition.<br>
 * This object defines the channel for a Kapua response message.<br>
 * The response message is used to perform interactive operations with the device (e.g. to send command to the device, to ask configurations...)
 * 
 * @since 1.0
 *
 */
public interface KapuaResponseChannel extends KapuaAppChannel
{

}
