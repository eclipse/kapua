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

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class AbstractProcessorTest extends Assert {

    private class AbstractProcessorImpl extends AbstractProcessor {
        /**
         * Creates a processor with the specified name (used by component metrics name)
         *
         * @param name
         */
        protected AbstractProcessorImpl(String name) {
            super(name);
        }

        @Override
        public void processMessage(Object message) {

        }
    }

    @Test
    public void abstractProcessorTest() {
        String[] names = {null, "", "name-1", "name", "name_123_!@#", "!@name 999", "NaMe<555>", "   name 23.,,,.'||     ", "a", "name   ,.name<>4567 name -0     name [] name", "12   43naME &*(0name_9("};
        for (String name : names) {
            AbstractProcessor abstractProcessor = new AbstractProcessorImpl(name);

            assertEquals("Expected and actual values should be the same.", name, abstractProcessor.name);
        }
    }
}