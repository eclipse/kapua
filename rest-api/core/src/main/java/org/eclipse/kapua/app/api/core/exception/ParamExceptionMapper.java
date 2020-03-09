/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.app.api.core.exception.model.IllegalArgumentExceptionInfo;
import org.eclipse.kapua.commons.util.KapuaExceptionUtils;

import org.glassfish.jersey.server.ParamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class ParamExceptionMapper implements ExceptionMapper<ParamException> {

    private static final Logger LOG = LoggerFactory.getLogger(ParamExceptionMapper.class);

    @Override
    public Response toResponse(ParamException paramException) {
        KapuaIllegalArgumentException kapuaIllegalArgumentException;
        KapuaException cause = KapuaExceptionUtils.extractKapuaException(paramException);
        if (cause instanceof KapuaIllegalArgumentException) {
            kapuaIllegalArgumentException = (KapuaIllegalArgumentException) cause;
        } else {
            kapuaIllegalArgumentException = new KapuaIllegalArgumentException(paramException.getParameterName(), null);
        }
        LOG.error("Illegal argument exception!", paramException);
        return Response//
                .status(Status.BAD_REQUEST) //
                .entity(new IllegalArgumentExceptionInfo(Status.BAD_REQUEST, kapuaIllegalArgumentException)) //
                .build();
    }

}
