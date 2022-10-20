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
package org.eclipse.kapua.app.api.web;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;
import javax.xml.bind.JAXBContext;


@Category(JUnitTests.class)
public class RestApiJAXBContextProviderTest {

    RestApiJAXBContextProvider restApiJAXBContextProvider;
    Providers providers;
    ContextResolver<JAXBContext> contextResolver;
    JAXBContext jaxbContext;

    @Before
    public void initialize() {
        restApiJAXBContextProvider = new RestApiJAXBContextProvider();
        providers = Mockito.mock(Providers.class);
        contextResolver = Mockito.mock(ContextResolver.class);
        jaxbContext = Mockito.mock(JAXBContext.class);
    }

    @Test
    public void getJAXBContextNullProvidersTest() {
        restApiJAXBContextProvider.providers = null;

        try {
            restApiJAXBContextProvider.getJAXBContext();
            Assert.fail("KapuaException expected.");
        } catch (Exception e) {
            Assert.assertEquals("KapuaException expected.", "org.eclipse.kapua.KapuaException: An internal error occurred: Unable to find any provider..", e.toString());
        }
    }

    @Test
    public void getJAXBNullContextTest() {
        restApiJAXBContextProvider.providers = providers;

        Mockito.when(providers.getContextResolver(JAXBContext.class, MediaType.APPLICATION_XML_TYPE)).thenReturn(contextResolver);
        Mockito.when(contextResolver.getContext(JAXBContext.class)).thenReturn(null);

        try {
            restApiJAXBContextProvider.getJAXBContext();
            Assert.fail("KapuaException expected.");
        } catch (Exception e) {
            Assert.assertEquals("KapuaException expected.", "org.eclipse.kapua.KapuaException: An internal error occurred: Unable to get a JAXBContext..", e.toString());
        }
    }

    @Test
    public void getJAXBContextTest() throws KapuaException {
        restApiJAXBContextProvider.providers = providers;

        Mockito.when(providers.getContextResolver(JAXBContext.class, MediaType.APPLICATION_XML_TYPE)).thenReturn(contextResolver);
        Mockito.when(contextResolver.getContext(JAXBContext.class)).thenReturn(jaxbContext);

        Assert.assertTrue("True expected.", restApiJAXBContextProvider.getJAXBContext() instanceof JAXBContext);
    }
}