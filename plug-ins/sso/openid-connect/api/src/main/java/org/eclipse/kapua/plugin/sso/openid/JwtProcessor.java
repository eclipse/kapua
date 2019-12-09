/*******************************************************************************
 * Copyright (c) 2019, 2020 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.plugin.sso.openid;

import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDException;
import org.eclipse.kapua.plugin.sso.openid.exception.jwt.OpenIDJwtException;
import org.jose4j.jwt.consumer.JwtContext;

public interface JwtProcessor extends AutoCloseable {

    /**
     * Validates the JWT passed as parameter.
     *
     * @param jwt the String representing the JWT.
     * @return <tt>true</tt> if the validation succeeds, <tt>false</tt> otherwise.
     * @throws OpenIDJwtException if the validation fails.
     */
    boolean validate(final String jwt) throws OpenIDException;

    /**
     * Process the JWT and generate a JwtContext object.
     *
     * @param jwt the String representing the JWT.
     * @return a JwtContext object.
     * @throws OpenIDJwtException if JWT processing fails.
     */
    JwtContext process(final String jwt) throws OpenIDException;
}
