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
package org.eclipse.kapua.commons.model;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.util.RandomUtils;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;


@Category(JUnitTests.class)
@RunWith(value = Parameterized.class)
public class AbstractKapuaEntityCreatorTest {

    private final static Random RANDOM = RandomUtils.getInstance();

    private final BigInteger eid;

    @Parameters
    public static Iterable<Object[]> eids() {
        return Arrays.asList(new Object[]{new BigInteger(64, RANDOM)}, new Object[]{null});
    }

    public AbstractKapuaEntityCreatorTest(BigInteger eid) {
        this.eid = eid;
    }

    private class ActualKapuaEntityCreator<E extends KapuaEntity> extends AbstractKapuaEntityCreator<E> {

        public ActualKapuaEntityCreator(KapuaId scopeId) {
            super(scopeId);
        }

        public ActualKapuaEntityCreator(AbstractKapuaEntityCreator<E> abstractEntityCreator) {
            super(abstractEntityCreator);
        }
    }

    @Test
    public void constructorScopeIdTest() {
        KapuaId scopeId = new KapuaEid(eid);
        AbstractKapuaEntityCreator kapuaEntityCreator = new ActualKapuaEntityCreator(scopeId);
        AbstractKapuaEntityCreator kapuaCopyEntityCreator = new ActualKapuaEntityCreator(kapuaEntityCreator);
        Assert.assertEquals("Those entities should have the same scopeId.", kapuaEntityCreator.getScopeId(), kapuaCopyEntityCreator.getScopeId());
    }
}
