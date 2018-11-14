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
package org.eclipse.kapua;

public class DeviceMenagementException extends KapuaException{

    private static final long serialVersionUID = -6804774877886678863L;

    public DeviceMenagementException(DeviceMenagementErrorCodes code) {
        super(code);
    }

}
