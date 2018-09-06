/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.connector;

import org.eclipse.kapua.KapuaErrorCode;
import org.eclipse.kapua.KapuaException;

public class KapuaProcessorException extends KapuaException {

    private static final long serialVersionUID = 381442941741219811L;

    public KapuaProcessorException(KapuaErrorCode code) {
        super(code);
    }

    public KapuaProcessorException(KapuaErrorCode code, Object... arguments) {
        super(code, arguments);
    }

    public KapuaProcessorException(KapuaErrorCode code, Throwable t) {
        super(code, t);
    }

}
