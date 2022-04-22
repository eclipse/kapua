/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua;

/**
 * @since 1.1.0
 * @deprecated Since 2.0.0. This exception is not used but in tests.
 */
@Deprecated
public class KapuaExceptionWithoutBundle extends KapuaException {
    public KapuaExceptionWithoutBundle(KapuaErrorCode code) {
        super(code);
    }

    public KapuaExceptionWithoutBundle(KapuaErrorCode code, Object... arguments) {
        super(code, arguments);
    }

    public KapuaExceptionWithoutBundle(KapuaErrorCode code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

    @Override
    protected String getKapuaErrorMessagesBundle() {
        return "non-existing-file.properties";
    }
}
