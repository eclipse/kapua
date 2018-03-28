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
