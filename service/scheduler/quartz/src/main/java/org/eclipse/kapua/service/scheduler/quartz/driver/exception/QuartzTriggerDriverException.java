/*******************************************************************************
 * Copyright (c) 2019, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.scheduler.quartz.driver.exception;

import org.eclipse.kapua.KapuaException;

import javax.validation.constraints.NotNull;

/**
 * The {@code abstract} {@link KapuaException} class for {@link org.eclipse.kapua.service.scheduler.quartz.driver.QuartzTriggerDriver} {@link Exception}s
 */
public abstract class QuartzTriggerDriverException extends KapuaException {

    private static final String KAPUA_ERROR_MESSAGES = "quartz-trigger-driver-error-messages";

    public QuartzTriggerDriverException(QuartzTriggerDriverErrorCodes code, Object... arguments) {
        this(code, null, arguments);
    }

    public QuartzTriggerDriverException(@NotNull QuartzTriggerDriverErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

    @Override
    protected String getKapuaErrorMessagesBundle() {
        return KAPUA_ERROR_MESSAGES;
    }
}
