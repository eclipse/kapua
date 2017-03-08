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
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.model;

import javax.xml.bind.annotation.XmlEnum;

/**
 * Data index by options
 * 
 * @since 1.0
 */
@XmlEnum
public enum DataIndexBy
{
    /**
     * Server timestamp.<br>
     * The message will be indexed by the timestamp of the server at the processing time
     */
    SERVER_TIMESTAMP,
    /**
     * Device timestamp.<br>
     * The message will be indexed by the timestamp of the device (capturedOn message field)
     */
    DEVICE_TIMESTAMP;
}
