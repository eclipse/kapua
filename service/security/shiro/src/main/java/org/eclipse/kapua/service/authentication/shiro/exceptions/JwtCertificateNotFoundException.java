/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.shiro.exceptions;

import org.eclipse.kapua.service.authentication.KapuaAuthenticationErrorCodes;

public class JwtCertificateNotFoundException extends AuthenticationRuntimeException {

    public JwtCertificateNotFoundException() {
        super(KapuaAuthenticationErrorCodes.JWT_CERTIFICATE_NOT_FOUND);
    }

}
