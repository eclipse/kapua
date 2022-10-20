/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.model;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;


@Category(JUnitTests.class)
@RunWith(value = Parameterized.class)
public class AbstractKapuaNamedEntityTest {

    private final String name;

    private final String description;

    private final KapuaId scopeId = new KapuaEid();

    @Parameters
    public static Collection<Object[]> names() {
        return Arrays.asList(new Object[][]{
                {"", "",},
                {"name", "description"},
                {"NAME", "DESCRIPTION"},
                {"&name%", "#description!"},
                {"1234", "5678"},
                {"make space", "make space"},
        });
    }

    public AbstractKapuaNamedEntityTest(String name, String description) {
        this.name = name;
        this.description = description;
    }

    private class ActualKapuaNamedEntity extends AbstractKapuaNamedEntity {

        @Override
        public String getType() {
            return null;
        }

        public ActualKapuaNamedEntity() {
            super();
        }

        public ActualKapuaNamedEntity(KapuaId scopeId) {
            super(scopeId);
        }

        public ActualKapuaNamedEntity(KapuaId scopeId, String name) {
            super(scopeId, name);
        }

        public ActualKapuaNamedEntity(KapuaNamedEntity kapuaNamedEntity) {
            super(kapuaNamedEntity);
        }
    }

    @Test
    public void abstractKapuaNamedEntityScopeIdTest() {
        AbstractKapuaNamedEntity namedEntity = new ActualKapuaNamedEntity();
        namedEntity.setScopeId(scopeId);
        Assert.assertEquals("Expected and actual values should be the same!", scopeId, namedEntity.getScopeId());
    }

    @Test
    public void abstractKapuaNamedEntityNameTest() {
        AbstractKapuaNamedEntity namedEntity = new ActualKapuaNamedEntity(scopeId);
        namedEntity.setName(name);
        AbstractKapuaNamedEntity namedCopyEntity = new ActualKapuaNamedEntity(scopeId, name);
        Assert.assertEquals("Expected and actual values should be the same!", namedEntity.getName(), namedCopyEntity.getName());
    }

    @Test
    public void abstractKapuaNamedEntityIdEntityTest() {
        AbstractKapuaNamedEntity namedEntity = new ActualKapuaNamedEntity(scopeId);
        namedEntity.setName(name);
        namedEntity.setDescription(description);
        AbstractKapuaNamedEntity namedCopyEntity = new ActualKapuaNamedEntity(namedEntity);
        Assert.assertEquals("Expected and actual values should be the same!", namedEntity.getName(), namedCopyEntity.getName());
        Assert.assertEquals("Expected and actual values should be the same!", namedEntity.getDescription(), namedCopyEntity.getDescription());
    }
}
