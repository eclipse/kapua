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
package org.eclipse.kapua.connector;

import org.eclipse.kapua.KapuaErrorCode;

/**
 * Connector error codes.
 * 
 * @since 1.0
 *
 */
public enum ConverterErrorCodes implements KapuaErrorCode {
    /**
     * Message Convertion Error
     */
    CONVERTION_ERROR,

    /**
     * No topic
     */
    CONVERTION_NO_TOPIC

}
