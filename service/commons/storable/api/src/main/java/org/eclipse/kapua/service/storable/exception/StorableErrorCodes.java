/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.storable.exception;

import org.eclipse.kapua.KapuaErrorCode;

/**
 * {@link StorableException} {@link KapuaErrorCode}s
 *
 * @since 1.3.0
 */
public enum StorableErrorCodes implements KapuaErrorCode {

    /**
     * See {@link InvalidValueMappingException}
     *
     * @since 1.3.0
     */
    INVALID_VALUE,

    /**
     * See {@link UnsupportedTypeMappingException}
     *
     * @since 1.3.0
     */
    UNSUPPORTED_TYPE
}
