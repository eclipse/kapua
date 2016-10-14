/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat - initial implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.datastore;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;

public class DataStoreOperationNotSupportedException extends KapuaException {

    public DataStoreOperationNotSupportedException(Object... arguments) {
        super(KapuaErrorCodes.OPERATION_NOT_SUPPORTED, arguments);
    }

    public DataStoreOperationNotSupportedException(Throwable cause, Object... arguments) {
        super(KapuaErrorCodes.OPERATION_NOT_SUPPORTED, cause, arguments);
    }

}
