/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.app.api.core.exception.model.EntityNotFoundExceptionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class KapuaEntityNotFoundExceptionMapper implements ExceptionMapper<KapuaEntityNotFoundException> {

    private static final Logger LOG = LoggerFactory.getLogger(KapuaEntityNotFoundExceptionMapper.class);

    @Override
    public Response toResponse(KapuaEntityNotFoundException kapuaException) {
        LOG.error("Entity not found exception!", kapuaException);
        return Response//
                .status(Status.NOT_FOUND) //
                .entity(new EntityNotFoundExceptionInfo(Status.NOT_FOUND, kapuaException)) //
                .build();
    }
}
