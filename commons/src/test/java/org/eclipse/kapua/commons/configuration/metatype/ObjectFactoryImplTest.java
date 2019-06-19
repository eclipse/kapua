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
public class ObjectFactoryImplTest extends Assert {

    @Test
    public void testCreateTicon() {
        ObjectFactoryImpl factoryImpl = new ObjectFactoryImpl();
        Assert.assertNotNull(factoryImpl.createTicon());
    }

    @Test
    public void testCreateTattribute() {
        ObjectFactoryImpl factoryImpl = new ObjectFactoryImpl();
        Assert.assertNotNull(factoryImpl.createTattribute());
    }

    @Test
    public void testCreateTmetadata() {
        ObjectFactoryImpl factoryImpl = new ObjectFactoryImpl();
        Assert.assertNotNull(factoryImpl.createTmetadata());
    }

    @Test
    public void testCreateTdesignate() {
        ObjectFactoryImpl factoryImpl = new ObjectFactoryImpl();
        Assert.assertNotNull(factoryImpl.createTdesignate());
    }

    @Test
    public void testCreateTad() {
        ObjectFactoryImpl factoryImpl = new ObjectFactoryImpl();
        Assert.assertNotNull(factoryImpl.createTad());
    }

    @Test
    public void testCreateTobject() {
        ObjectFactoryImpl factoryImpl = new ObjectFactoryImpl();
        Assert.assertNotNull(factoryImpl.createTobject());
    }

    @Test
    public void testCreateTocd() {
        ObjectFactoryImpl factoryImpl = new ObjectFactoryImpl();
        Assert.assertNotNull(factoryImpl.createTocd());
    }

    @Test
    public void testCreateToption() {
        ObjectFactoryImpl factoryImpl = new ObjectFactoryImpl();
        Assert.assertNotNull(factoryImpl.createToption());
    }

    @Test
    public void testCreateMetaData() {
        TmetadataImpl value = new TmetadataImpl();
        ObjectFactoryImpl factoryImpl = new ObjectFactoryImpl();
        Assert.assertNotNull(factoryImpl.createMetaData(value));
    }
}