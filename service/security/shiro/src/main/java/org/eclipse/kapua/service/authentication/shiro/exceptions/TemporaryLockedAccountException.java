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
