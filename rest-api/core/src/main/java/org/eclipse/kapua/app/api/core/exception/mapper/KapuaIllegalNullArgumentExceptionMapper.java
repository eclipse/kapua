/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
