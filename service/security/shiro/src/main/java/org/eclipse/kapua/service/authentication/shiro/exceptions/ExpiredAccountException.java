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

import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.shiro.authc.DisabledAccountException;

public class ExpiredAccountException extends DisabledAccountException {

    public ExpiredAccountException(final Date expirationDate) {
        super("This credential has been locked out until " + (expirationDate != null ? DateTimeFormatter.ISO_INSTANT.format(expirationDate.toInstant()) : "<null>"));

    }
}
