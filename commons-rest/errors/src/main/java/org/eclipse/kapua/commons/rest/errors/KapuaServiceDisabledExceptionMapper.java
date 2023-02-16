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
package org.eclipse.kapua.commons.rest.errors;

import org.eclipse.kapua.commons.rest.model.errors.ExceptionInfo;
import org.eclipse.kapua.commons.service.internal.KapuaServiceDisabledException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class KapuaServiceDisabledExceptionMapper implements ExceptionMapper<KapuaServiceDisabledException> {

    private static final Logger LOG = LoggerFactory.getLogger(KapuaServiceDisabledException.class);

    private static final Status STATUS = Status.NOT_FOUND;
    @Inject
    public ExceptionConfigurationProvider exceptionConfigurationProvider;

    @Override
    public Response toResponse(KapuaServiceDisabledException kapuaServiceDisabledException) {
        final boolean showStackTrace = exceptionConfigurationProvider.showStackTrace();
        LOG.error(kapuaServiceDisabledException.getMessage(), kapuaServiceDisabledException);
        return Response
                .status(STATUS)
                .entity(new ExceptionInfo(STATUS.getStatusCode(), kapuaServiceDisabledException, showStackTrace))
                .build();
    }

}