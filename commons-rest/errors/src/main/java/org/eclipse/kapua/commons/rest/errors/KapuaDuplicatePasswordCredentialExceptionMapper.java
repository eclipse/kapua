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
package org.eclipse.kapua.commons.rest.errors;

import org.eclipse.kapua.commons.rest.model.errors.ExceptionInfo;
import org.eclipse.kapua.service.authentication.exception.DuplicatedPasswordCredentialException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class KapuaDuplicatePasswordCredentialExceptionMapper implements ExceptionMapper<DuplicatedPasswordCredentialException> {

    private static final Logger LOG = LoggerFactory.getLogger(KapuaDuplicatePasswordCredentialExceptionMapper.class);

    private final boolean showStackTrace;

    @Inject
    public KapuaDuplicatePasswordCredentialExceptionMapper(ExceptionConfigurationProvider exceptionConfigurationProvider) {
        this.showStackTrace = exceptionConfigurationProvider.showStackTrace();
    }

    @Override
    public Response toResponse(DuplicatedPasswordCredentialException kapuaDuplicatePassException) {
        LOG.error(kapuaDuplicatePassException.getMessage(), kapuaDuplicatePassException);
        return Response
                .status(Status.CONFLICT)
                .entity(new ExceptionInfo(Status.CONFLICT.getStatusCode(), kapuaDuplicatePassException, showStackTrace))
                .build();
    }

}
