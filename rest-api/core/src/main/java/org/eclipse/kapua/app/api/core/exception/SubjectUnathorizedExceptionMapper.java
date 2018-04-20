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

import org.eclipse.kapua.app.api.core.exception.model.SubjectUnauthorizedExceptionInfo;
import org.eclipse.kapua.service.authorization.shiro.exception.SubjectUnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SubjectUnathorizedExceptionMapper implements ExceptionMapper<SubjectUnauthorizedException> {

    private static final Logger LOG = LoggerFactory.getLogger(SubjectUnathorizedExceptionMapper.class);

    @Override
    public Response toResponse(SubjectUnauthorizedException exception) {
        LOG.error("Subject not authorized!", exception);
        return Response//
                .status(Status.FORBIDDEN) //
                .entity(new SubjectUnauthorizedExceptionInfo(Status.FORBIDDEN, exception)) //
                .build();
    }

}
