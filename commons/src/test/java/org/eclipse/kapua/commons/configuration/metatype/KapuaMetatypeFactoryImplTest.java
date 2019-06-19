/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.configuration.metatype;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaMetatypeFactoryImplTest extends Assert {

    @Test
    public void testKapuaTocd() {
        KapuaMetatypeFactoryImpl factory = new KapuaMetatypeFactoryImpl();
        Assert.assertNotNull(factory.newKapuaTocd());
    }

    @Test
    public void testkapuaTad() {
        KapuaMetatypeFactoryImpl factory = new KapuaMetatypeFactoryImpl();
        Assert.assertNotNull(factory.newKapuaTad());
    }

    @Test
    public void testKapuaTscalar() {
        KapuaMetatypeFactoryImpl factory = new KapuaMetatypeFactoryImpl();
        Assert.assertNotNull(factory.newKapuaTscalar("String"));
    }

    @Test
    public void testNewKapuaToption() {
        KapuaMetatypeFactoryImpl factory = new KapuaMetatypeFactoryImpl();
        Assert.assertNotNull(factory.newKapuaToption());
    }

    @Test
    public void testNewKapuaTicon() {
        KapuaMetatypeFactoryImpl factory = new KapuaMetatypeFactoryImpl();
        Assert.assertNotNull(factory.newKapuaTicon());
    }

    @Test
    public void testNewKapuaTmetadata() {
        KapuaMetatypeFactoryImpl factory = new KapuaMetatypeFactoryImpl();
        Assert.assertNotNull(factory.newKapuaTmetadata());
    }

    @Test
    public void testNewKapuaTdesignate() {
        KapuaMetatypeFactoryImpl factory = new KapuaMetatypeFactoryImpl();
        Assert.assertNotNull(factory.newKapuaTdesignate());
    }

    @Test
    public void testNewKapuaTobject() {
        KapuaMetatypeFactoryImpl factory = new KapuaMetatypeFactoryImpl();
        Assert.assertNotNull(factory.newKapuaTobject());
    }
}
