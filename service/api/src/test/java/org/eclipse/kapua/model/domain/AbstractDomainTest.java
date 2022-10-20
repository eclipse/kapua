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
package org.eclipse.kapua.model.domain;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.HashSet;
import java.util.Set;


@Category(JUnitTests.class)
public class AbstractDomainTest {

    private class AbstractDomainImpl extends AbstractDomain {

        @Override
        public String getName() {
            return "Domain Name";
        }

        @Override
        public Set<Actions> getActions() {
            Set<Actions> actionsSet = new HashSet<>();
            actionsSet.add(Actions.read);
            actionsSet.add(Actions.write);
            actionsSet.add(Actions.delete);
            actionsSet.add(Actions.connect);
            actionsSet.add(Actions.execute);
            return actionsSet;
        }

        @Override
        public boolean getGroupable() {
            return false;
        }
    }

    @Test
    public void equalsTest() {
        AbstractDomain abstractDomain1 = new AbstractDomainImpl();
        AbstractDomain abstractDomain2 = new AbstractDomainImpl();
        Object[] objects = {0, 10, 100000, "String", 'c', -10, -1000000000, -100000000000L, 10L, 10.0f, null, 10.10d, true, false};

        Assert.assertTrue("True expected.", abstractDomain1.equals(abstractDomain1));
        Assert.assertFalse("False expected.", abstractDomain1.equals(null));
        for (Object object : objects) {
            Assert.assertFalse("False expected.", abstractDomain1.equals(object));
        }
        Assert.assertTrue("True expected.", abstractDomain1.equals(abstractDomain2));
    }

    @Test
    public void hashCodeTest() {
        AbstractDomain abstractDomain = new AbstractDomainImpl();

        Assert.assertThat("Instance of Integer expected.", abstractDomain.hashCode(), IsInstanceOf.instanceOf(Integer.class));
    }
}
