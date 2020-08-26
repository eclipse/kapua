/*******************************************************************************
 * Copyright (c) 2018, 2020 Eurotech and/or its affiliates and others
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

import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.shiro.authc.DisabledAccountException;

public class ExpiredAccountException extends DisabledAccountException {

    public ExpiredAccountException(final Date expirationDate) {
        super("This credential has been locked out until " + (expirationDate != null ? DateTimeFormatter.ISO_INSTANT.format(expirationDate.toInstant()) : "<null>"));

    }
}
