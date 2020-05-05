/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.sso;

import org.eclipse.kapua.sso.exception.SsoException;
import org.eclipse.kapua.sso.exception.jwt.SsoJwtException;
import org.jose4j.jwt.consumer.JwtContext;

public interface JwtProcessor extends AutoCloseable {

    /**
     * Validates the JWT passed as parameter.
     *
     * @param jwt the String representing the JWT.
     * @return <tt>true</tt> if the validation succeeds, <tt>false</tt> otherwise.
     * @throws SsoJwtException if the validation fails.
     */
    boolean validate(final String jwt) throws SsoException;

    /**
     * Process the JWT and generate a JwtContext object.
     *
     * @param jwt the String representing the JWT.
     * @return a JwtContext object.
     * @throws SsoJwtException if JWT processing fails.
     */
    JwtContext process(final String jwt) throws SsoException;
}
