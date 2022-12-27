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
package org.eclipse.kapua.app.api.web;

import org.eclipse.kapua.app.api.core.settings.KapuaApiCoreSetting;
import org.eclipse.kapua.app.api.core.settings.KapuaApiCoreSettingKeys;
import org.eclipse.kapua.commons.rest.errors.ExceptionConfigurationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class ExceptionConfigurationProviderImpl implements ExceptionConfigurationProvider {
    private final boolean showStackTrace = KapuaApiCoreSetting.getInstance().getBoolean(KapuaApiCoreSettingKeys.API_EXCEPTION_STACKTRACE_SHOW, false);
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ExceptionConfigurationProviderImpl() {
        logger.debug("Initialized with showStackTrace={}", showStackTrace);
    }

    @Override
    public boolean showStackTrace() {
        return showStackTrace;
    }
}
