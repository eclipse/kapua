/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
