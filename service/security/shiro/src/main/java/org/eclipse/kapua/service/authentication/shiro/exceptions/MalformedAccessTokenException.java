/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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

import org.apache.shiro.authc.CredentialsException;

public class MalformedAccessTokenException extends CredentialsException {

    private static final long serialVersionUID = 3962802706975119699L;

    public MalformedAccessTokenException() {
        super("The provided access token is malformed");
    }

}
