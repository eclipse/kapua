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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro.exceptions;

import org.apache.shiro.authc.LockedAccountException;

import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TemporaryLockedAccountException extends LockedAccountException {

    private static final long serialVersionUID = 1L;

    public TemporaryLockedAccountException(final Date lockoutExpiration) {
        super("This credential has been locked out until " + (lockoutExpiration != null ? DateTimeFormatter.ISO_INSTANT.format(lockoutExpiration.toInstant()) : "<null>"));
    }
}
