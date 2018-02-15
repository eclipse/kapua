/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.api.resources.v1.resources.exception;

import org.eclipse.kapua.KapuaErrorCode;

public enum RestApiErrorCodes implements KapuaErrorCode {
    /**
     * When a resource receive a request, but the {@link org.eclipse.kapua.commons.security.KapuaSession} is not populated.
     * This is likely to happen when the `shiro.ini` does not map the `kapuaAuthcAccessToken` as a request filter chain.
     */
    SESSION_NOT_POPULATED
}
