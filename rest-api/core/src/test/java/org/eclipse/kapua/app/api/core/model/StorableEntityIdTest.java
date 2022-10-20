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
package org.eclipse.kapua.app.api.core.model;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class StorableEntityIdTest {

    @Test
    public void storableEntityIdWithParameterTest() {
        String[] ids = {"id", "", "id123", "id!@#$%^123&*<>|", "  id 1  2;':...,, id ()   ", "        !@#id '>? id ID ,,12$#  ", "id*(-01.23 idID 0123!@@,.,,   "};

        for (String id : ids) {
            StorableEntityId storableEntityId = new StorableEntityId(id);
            Assert.assertEquals("Expected and actual values should be the same.", id, storableEntityId.getId());
            Assert.assertEquals("Expected and actual values should be the same.", id, storableEntityId.toString());
        }
    }

    @Test
    public void storableEntityIdWithNullParameterTest() {
        StorableEntityId storableEntityId = new StorableEntityId(null);

        Assert.assertNull("Null expected.", storableEntityId.getId());
        Assert.assertNull("Null expected.", storableEntityId.toString());
    }

    @Test
    public void storableEntityIdWithoutParameterTest() {

        StorableEntityId storableEntityId = new StorableEntityId();
        Assert.assertNull("Null expected.", storableEntityId.getId());
        Assert.assertNull("Null expected.", storableEntityId.toString());
    }

    @Test
    public void setAndGetIdTest() {
        StorableEntityId storableEntityId1 = new StorableEntityId();
        StorableEntityId storableEntityId2 = new StorableEntityId("id");

        String[] newIds = {null, "", "idNEW12<>$*%7464", "", "id123-NEW 1  ^54IDnew 32^$%$", "idNEW    !@#$%^  123&*<>|  ", "  id 12NEW;':...,, id ()   ", "!@#id '>? id ID ,,12NEW1$#  ", "idNew*(-0123 idID 0123!@@,.,,"};

        for (String id : newIds) {
            storableEntityId1.setId(id);
            storableEntityId2.setId(id);

            Assert.assertEquals("Expected and actual values should be the same.", id, storableEntityId1.getId());
            Assert.assertEquals("Expected and actual values should be the same.", id, storableEntityId1.toString());

            Assert.assertEquals("Expected and actual values should be the same.", id, storableEntityId2.getId());
            Assert.assertEquals("Expected and actual values should be the same.", id, storableEntityId2.toString());
        }
    }
} 