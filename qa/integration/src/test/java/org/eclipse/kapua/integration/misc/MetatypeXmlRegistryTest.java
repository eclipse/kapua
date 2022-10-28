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
package org.eclipse.kapua.integration.misc;

import org.eclipse.kapua.model.config.metatype.KapuaTad;
import org.eclipse.kapua.model.config.metatype.KapuaTicon;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.config.metatype.KapuaToption;
import org.eclipse.kapua.model.config.metatype.KapuaTmetadata;
import org.eclipse.kapua.model.config.metatype.KapuaTdesignate;
import org.eclipse.kapua.model.config.metatype.KapuaTobject;

import org.eclipse.kapua.model.config.metatype.MetatypeXmlRegistry;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class MetatypeXmlRegistryTest {

    MetatypeXmlRegistry metatypeXmlRegistry;

    @Before
    public void createInstanceOfClass() {
        metatypeXmlRegistry = new MetatypeXmlRegistry();
    }

    @Test
    public void newKapuaTocdTest() {
        Assert.assertThat("Instance of KapuaTocd expected.", metatypeXmlRegistry.newKapuaTocd(), IsInstanceOf.instanceOf(KapuaTocd.class));
    }

    @Test
    public void newKapuaTadTest() {
        Assert.assertThat("Instance of KapuaTad expected.", metatypeXmlRegistry.newKapuaTad(), IsInstanceOf.instanceOf(KapuaTad.class));
    }

    @Test
    public void newKapuaTiconTest() {
        Assert.assertThat("Instance of KapuaTicon expected.", metatypeXmlRegistry.newKapuaTicon(), IsInstanceOf.instanceOf(KapuaTicon.class));
    }

    @Test
    public void newKapuaToptionTest() {
        Assert.assertThat("Instance of KapuaToption expected.", metatypeXmlRegistry.newKapuaToption(), IsInstanceOf.instanceOf(KapuaToption.class));
    }

    @Test
    public void newKapuaTmetadataTest() {
        Assert.assertThat("Instance of KapuaTmetadata expected.", metatypeXmlRegistry.newKapuaTmetadata(), IsInstanceOf.instanceOf(KapuaTmetadata.class));
    }

    @Test
    public void newKapuaTdesignateTest() {
        Assert.assertThat("Instance of KapuaTdesignate expected.", metatypeXmlRegistry.newKapuaTdesignate(), IsInstanceOf.instanceOf(KapuaTdesignate.class));
    }

    @Test
    public void newKapuaTobjectTest() {
        Assert.assertThat("Instance of KapuaTobject expected.", metatypeXmlRegistry.newKapuaTobject(), IsInstanceOf.instanceOf(KapuaTobject.class));
    }
}
