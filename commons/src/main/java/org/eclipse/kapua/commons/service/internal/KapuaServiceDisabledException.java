/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
