/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.shiro;

import javax.inject.Singleton;

import org.eclipse.kapua.model.id.KapuaId;

@Singleton
public class CommonTestData {

    /**
     * A flag to mark that an exception was thrown in the preceding steps.
     */
    public boolean exceptionCaught;
    public long count;
    public int intValue;
    public String stringValue;
    public KapuaId scopeId;

    public void clearData() {
        exceptionCaught = false;
        count = 0;
        intValue = 0;
        stringValue = "";
        scopeId = null;
    }
}
