/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
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

import org.junit.Assert;
import org.junit.Test;

public class KapuaExceptionTest {

    @Test
    public void test() {
        KapuaException ke = new KapuaException(KapuaErrorCodes.ENTITY_NOT_FOUND, "user", 1);
        Assert.assertEquals("The entity of type user with id/name 1 was not found.",
                ke.getMessage());

        ke = KapuaException.internalError("ciao");
        Assert.assertEquals("An internal error occurred: ciao.",
                ke.getMessage());

        ke = KapuaException.internalError(new NullPointerException());
        Assert.assertEquals("An internal error occurred: java.lang.NullPointerException.",
                ke.getMessage());

        ke = new KapuaException(new KapuaErrorCode() {

            @Override
            public String name() {
                return "MISSING";
            }
        }, "abc", 1);
        Assert.assertEquals("Error: abc,1", ke.getMessage());
    }
}
