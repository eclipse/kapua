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
