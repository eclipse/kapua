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

import org.eclipse.kapua.service.authentication.shiro.KapuaAuthenticationException;

public class KapuaAuthenticationExceptionMapper implements ExceptionMapper<KapuaAuthenticationException> {

    @Override
    public Response toResponse(KapuaAuthenticationException exception) {
        return Response.status(Status.UNAUTHORIZED).build();
    }

}
