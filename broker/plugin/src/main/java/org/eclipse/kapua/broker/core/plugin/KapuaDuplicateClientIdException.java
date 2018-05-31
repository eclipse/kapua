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
package org.eclipse.kapua.broker.core.plugin;

import org.eclipse.kapua.KapuaException;

public class KapuaDuplicateClientIdException extends KapuaException {

    private static final long serialVersionUID = 1751650664486096457L;

    public KapuaDuplicateClientIdException(String clientId) {
        super(KapuaBrokerErrorCodes.DUPLICATED_CLIENT_ID, clientId);
    }

}
