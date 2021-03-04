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
package org.eclipse.kapua.app.api.web;

import org.eclipse.kapua.qa.markers.Categories;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import javax.servlet.ServletContextEvent;

@Category(Categories.junitTests.class)
public class RestApiListenerTest extends Assert {

    RestApiListener restApiListener;
    ServletContextEvent servletContextEvent;

    @Before
    public void initialize() {
        restApiListener = new RestApiListener();
        servletContextEvent = Mockito.mock(ServletContextEvent.class);
    }

    @Test
    public void contextInitializedTest() {
        try {
            restApiListener.contextInitialized(servletContextEvent);
        } catch (Exception e) {
            fail("Exception not expected.");
        }
    }

    @Test
    public void contextInitializedNullTest() {
        try {
            restApiListener.contextInitialized(null);
        } catch (Exception e) {
            fail("Exception not expected.");
        }
    }

    @Test
    public void contextDestroyedTest() {
        try {
            restApiListener.contextDestroyed(servletContextEvent);
        } catch (Exception e) {
            fail("Exception not expected.");
        }
    }

    @Test
    public void contextDestroyedNullTest() {
        try {
            restApiListener.contextDestroyed(null);
        } catch (Exception e) {
            fail("Exception not expected.");
        }
    }
}