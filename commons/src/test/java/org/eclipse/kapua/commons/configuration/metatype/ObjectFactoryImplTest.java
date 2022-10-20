/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.configuration.metatype;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class ObjectFactoryImplTest {

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
