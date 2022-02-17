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
package org.eclipse.kapua.broker.core.listener;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class AbstractListenerTest extends Assert {

    private class AbstractListenerImpl extends AbstractListener {
        protected AbstractListenerImpl(String name) {
            super(name);
        }

        protected AbstractListenerImpl(String metricComponentName, String name) {
            super(metricComponentName, name);
        }
    }

    AbstractListener abstractListenerWithOneParameter, abstractListenerWithTwoParameters;
    String[] names, metricComponentNames;

    @Before
    public void initialize() {
        abstractListenerWithOneParameter = new AbstractListenerImpl("Abstract Listener");
        abstractListenerWithTwoParameters = new AbstractListenerImpl("Component name", "Abstract Listener");
        names = new String[]{null, "", "name-1", "name", "name_123_!@#", "!@name 999", "NaMe<555>", "   name 23.,,,.'||     ", "a", "name   ,.name<>4567 name -0     name [] name", "12   43naME &*(0name_9("};
        metricComponentNames = new String[]{null, "", "metric    123<.com,,ponentNAME 955':;;", "a", "#21%$#met-tric;'|| name       component,|,.", "MeTrIc09)_=-0", "-0 metric name;',.COMPONENT", "name()   _  compone-net ;;,z"};
    }

    @Test
    public void abstractListenerNameParameterTest() {
        for (String name : names) {
            AbstractListener abstractListener = new AbstractListenerImpl(name);
            assertEquals("Expected and actual values should be the same.", name, abstractListener.name);
        }
    }

    @Test
    public void abstractListenerMetricComponentNameAndNameParameterTest() {
        for (String name : names) {
            for (String metricComponentName : metricComponentNames) {
                AbstractListener abstractListener = new AbstractListenerImpl(metricComponentName, name);
                assertEquals("Expected and actual values should be the same.", name, abstractListener.name);
            }
        }
    }

    @Test
    public void registerCounterTest() {
        assertThat("Instance of Counter expected.", abstractListenerWithOneParameter.registerCounter("Counter Name1", "Counter Name2", "Counter Name3"), IsInstanceOf.instanceOf(Counter.class));
        assertThat("Instance of Counter expected.", abstractListenerWithTwoParameters.registerCounter("Counter Name1", "Counter Name2", "Counter Name3"), IsInstanceOf.instanceOf(Counter.class));
    }

    @Test
    public void registerCounterOneNullNameTest() {
        assertThat("Instance of Counter expected.", abstractListenerWithOneParameter.registerCounter("CounterName1", null, "CounterName3"), IsInstanceOf.instanceOf(Counter.class));
        assertThat("Instance of Counter expected.", abstractListenerWithTwoParameters.registerCounter("CounterName1", null, "CounterName3"), IsInstanceOf.instanceOf(Counter.class));
    }

    @Test
    public void registerCounterNullTest() {
        try {
            abstractListenerWithOneParameter.registerCounter(null);
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("NullPointerException expected.", new NullPointerException().toString(), e.toString());
        }

        try {
            abstractListenerWithTwoParameters.registerCounter(null);
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("NullPointerException expected.", new NullPointerException().toString(), e.toString());
        }
    }

    @Test
    public void registerTimerTest() {
        assertThat("Instance of Timer expected.", abstractListenerWithOneParameter.registerTimer("Timer Name1", "Timer Name2", "Timer Name3"), IsInstanceOf.instanceOf(Timer.class));
        assertThat("Instance of Timer expected.", abstractListenerWithTwoParameters.registerTimer("Timer Name1", "Timer Name2", "Timer Name3"), IsInstanceOf.instanceOf(Timer.class));
    }

    @Test
    public void registerTimerOneNullNameTest() {
        assertThat("Instance of Timer expected.", abstractListenerWithOneParameter.registerTimer("TimerName1", null, "TimerName3"), IsInstanceOf.instanceOf(Timer.class));
        assertThat("Instance of Timer expected.", abstractListenerWithTwoParameters.registerTimer("TimerName1", null, "TimerName3"), IsInstanceOf.instanceOf(Timer.class));
    }

    @Test
    public void registerTimerNullTest() {
        try {
            abstractListenerWithOneParameter.registerTimer(null);
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("NullPointerException expected.", new NullPointerException().toString(), e.toString());
        }

        try {
            abstractListenerWithTwoParameters.registerTimer(null);
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("NullPointerException expected.", new NullPointerException().toString(), e.toString());
        }
    }
}