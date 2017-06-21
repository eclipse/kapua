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
package org.eclipse.kapua.service.authentication.shiro.exceptions;

import org.apache.shiro.authc.LockedAccountException;

import java.util.Date;

public class TemporaryLockedAccountException extends LockedAccountException {

    private Date lockoutExpiration;

    public TemporaryLockedAccountException(Date lockoutExpiration) {
        super("This credential has been locked out until " + lockoutExpiration.toString());
        this.lockoutExpiration = lockoutExpiration;
    }
}
