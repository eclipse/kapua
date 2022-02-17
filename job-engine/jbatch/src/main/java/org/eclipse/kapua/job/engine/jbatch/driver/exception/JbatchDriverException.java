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
package org.eclipse.kapua.job.engine.jbatch.driver.exception;

import org.eclipse.kapua.KapuaException;

public class JbatchDriverException extends KapuaException {

    private static final String KAPUA_ERROR_MESSAGES = "jbatch-driver-error-messages";

    public JbatchDriverException(JbatchDriverErrorCodes code, String jobName) {
        super(code, jobName);
    }

    public JbatchDriverException(JbatchDriverErrorCodes code, String jobName, Object... arguments) {
        super(code, jobName, arguments);
    }

    public JbatchDriverException(JbatchDriverErrorCodes code, Throwable cause, String jobName, Object... arguments) {
        super(code, cause, jobName, arguments);
    }

    @Override
    protected String getKapuaErrorMessagesBundle() {
        return KAPUA_ERROR_MESSAGES;
    }
}
