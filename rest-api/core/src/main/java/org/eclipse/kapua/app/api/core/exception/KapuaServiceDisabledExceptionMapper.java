/*******************************************************************************
 * Copyright (c) 2018, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.api.core.exception;

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
