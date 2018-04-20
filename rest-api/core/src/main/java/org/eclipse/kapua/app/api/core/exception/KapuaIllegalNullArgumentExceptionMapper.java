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

import org.eclipse.kapua.KapuaIllegalNullArgumentException;
import org.eclipse.kapua.app.api.core.exception.model.IllegalNullArgumentExceptionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class KapuaIllegalNullArgumentExceptionMapper implements ExceptionMapper<KapuaIllegalNullArgumentException> {

    private static final Logger LOG = LoggerFactory.getLogger(KapuaIllegalNullArgumentExceptionMapper.class);

    @Override
    public Response toResponse(KapuaIllegalNullArgumentException kapuaException) {
        LOG.error("Illegal null argument exception!", kapuaException);
        return Response//
                .status(Status.BAD_REQUEST) //
                .entity(new IllegalNullArgumentExceptionInfo(Status.BAD_REQUEST, kapuaException)) //
                .build();
    }
}
