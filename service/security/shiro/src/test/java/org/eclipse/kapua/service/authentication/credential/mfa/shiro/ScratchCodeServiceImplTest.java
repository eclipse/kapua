/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.credential.mfa.shiro;

import org.eclipse.kapua.qa.markers.Categories;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(Categories.junitTests.class)
public class ScratchCodeServiceImplTest extends Assert {

    @Test
    public void scratchCodeServiceImplTest() {
        ScratchCodeServiceImpl scratchCodeServiceImpl = new ScratchCodeServiceImpl();
        assertNotNull("Null not expected.", scratchCodeServiceImpl.getEntityManagerSession());
    }
}