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
package org.eclipse.kapua.commons.setting;

import org.eclipse.kapua.KapuaErrorCode;

/**
 * Setting error codes
 * 
 * since 1.0.0
 */
public enum KapuaSettingErrorCodes implements KapuaErrorCode {
    /**
     * Code for a malformed URL or resource name is given, and this causes an error on loading.
     * 
     * @since 1.0.0
     */
    INVALID_RESOURCE_NAME,

    /**
     * Code for error while parsing the configuration file.
     * 
     * @since 1.0.0
     */
    INVALID_RESOURCE_FILE,

    /**
     * Code for a correctly former URL that refer to a not existing file/folder
     */
    RESOURCE_NOT_FOUND,

}
