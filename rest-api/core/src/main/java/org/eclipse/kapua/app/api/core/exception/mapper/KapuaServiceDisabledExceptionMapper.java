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
package org.eclipse.kapua.app.api.core.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.eclipse.kapua.app.api.core.exception.model.ExceptionInfo;
import org.eclipse.kapua.commons.service.internal.KapuaServiceDisabledException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class KapuaServiceDisabledExceptionMapper implements ExceptionMapper<KapuaServiceDisabledException> {

    private static final Logger LOG = LoggerFactory.getLogger(KapuaServiceDisabledException.class);

    private static final Status STATUS = Status.NOT_FOUND;

    @Override
    public Response toResponse(KapuaServiceDisabledException kapuaException) {
        LOG.error("Service Disabled exception!", kapuaException);
        return Response
                .status(STATUS)
                .entity(new ExceptionInfo(STATUS, kapuaException.getCode(), kapuaException))
                .build();
    }

}
