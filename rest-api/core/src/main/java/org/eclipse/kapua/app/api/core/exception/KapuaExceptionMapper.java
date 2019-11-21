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
package org.eclipse.kapua.app.api.core.exception;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.api.core.exception.model.ExceptionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class KapuaExceptionMapper implements ExceptionMapper<KapuaException> {

    private static final Logger LOG = LoggerFactory.getLogger(KapuaExceptionMapper.class);

    @Override
    public Response toResponse(KapuaException kapuaException) {
        LOG.error("Generic Kapua exception!", kapuaException);
        return Response
                .serverError()
                .entity(new ExceptionInfo(Status.INTERNAL_SERVER_ERROR, kapuaException.getCode(), kapuaException))
                .build();
    }

}
