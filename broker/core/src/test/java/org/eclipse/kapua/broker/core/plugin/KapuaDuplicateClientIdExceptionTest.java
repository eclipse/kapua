/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.core.plugin;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaDuplicateClientIdExceptionTest extends Assert {

    @Test
    public void kapuaDuplicateClientIdException() {
        String[] clientIds = {null, "", "Client Id", "id1234567890", "id?><|+_)(*&^%$#@!~`", "client-id123", "321client_id!@", " cLiEnTID=99", ".client1!*", "CLIENT <id1> "};
        for (String id : clientIds) {
            KapuaDuplicateClientIdException kapuaDuplicateClientIdException = new KapuaDuplicateClientIdException(id);
            assertEquals("Expected and actual values should be the same.", KapuaBrokerErrorCodes.DUPLICATED_CLIENT_ID, kapuaDuplicateClientIdException.getCode());
            assertNull("Null expected.", kapuaDuplicateClientIdException.getCause());
            assertEquals("Expected and actual values should be the same.", "Error: " + id, kapuaDuplicateClientIdException.getMessage());
        }
    }

    @Test(expected = KapuaDuplicateClientIdException.class)
    public void throwingKapuaDuplicateClientIdExceptionTest() throws KapuaDuplicateClientIdException {
        throw new KapuaDuplicateClientIdException("Client Id");
    }

    @Test(expected = KapuaDuplicateClientIdException.class)
    public void throwingKapuaDuplicateNullClientIdExceptionTest() throws KapuaDuplicateClientIdException {
        throw new KapuaDuplicateClientIdException(null);
    }
}