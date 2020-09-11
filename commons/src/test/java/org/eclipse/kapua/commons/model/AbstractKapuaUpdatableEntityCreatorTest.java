/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.model;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Properties;

@Category(JUnitTests.class)
public class AbstractKapuaUpdatableEntityCreatorTest extends Assert {

    private class ActualKapuaUpdatableEntityCreator<E extends KapuaEntity> extends AbstractKapuaUpdatableEntityCreator<E> {

        public ActualKapuaUpdatableEntityCreator(KapuaId scopeId) {
            super(scopeId);
        }
    }

    @Test
    public void abstractKapuaUpdatableEntityCreatorScopeId() {
        KapuaId scopeId = new KapuaEid();
        AbstractKapuaUpdatableEntityCreator<AbstractKapuaEntity> kapuaUpdatableEntityCreator = new ActualKapuaUpdatableEntityCreator<>(scopeId);
        assertEquals("Actual and expected values are not the same!", scopeId, kapuaUpdatableEntityCreator.getScopeId());
    }

    @Test
    public void getEntityAttributesTest() {
        KapuaId scopeId = new KapuaEid();
        AbstractKapuaUpdatableEntityCreator<AbstractKapuaEntity> kapuaUpdatableEntityCreator = new ActualKapuaUpdatableEntityCreator<>(scopeId);
        Properties properties = new Properties();
        kapuaUpdatableEntityCreator.setEntityAttributes(properties);
        assertEquals("Actual and expected values are not the same!", properties, kapuaUpdatableEntityCreator.getEntityAttributes());
    }
}