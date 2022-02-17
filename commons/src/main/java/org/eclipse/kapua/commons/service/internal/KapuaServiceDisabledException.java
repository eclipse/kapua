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
package org.eclipse.kapua.commons.service.internal;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;

public class KapuaServiceDisabledException extends KapuaException {

    public KapuaServiceDisabledException(String serviceName) {
        super(KapuaErrorCodes.SERVICE_DISABLED, serviceName);
    }

}
