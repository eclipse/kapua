/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaEntityUniquenessException;
import org.eclipse.kapua.app.api.core.exception.model.EntityUniquenessExceptionInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class KapuaEntityUniquenessExceptionMapper implements ExceptionMapper<KapuaEntityUniquenessException> {

    private static final Logger LOG = LoggerFactory.getLogger(KapuaEntityUniquenessExceptionMapper.class);

    @Override
    public Response toResponse(KapuaEntityUniquenessException kapuaException) {
        LOG.error("Entity uniqueness exception!", kapuaException);
        return Response//
                       .status(Status.fromStatusCode(409)) //
                       .entity(new EntityUniquenessExceptionInfo(Status.fromStatusCode(409), kapuaException)) //
                       .build();
    }

}
