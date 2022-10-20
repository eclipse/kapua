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
package org.eclipse.kapua.commons.model.query.predicate;

import org.eclipse.kapua.model.query.predicate.QueryPredicate;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;


@Category(JUnitTests.class)
public class OrPredicateImplTest {

    @Test
    public void orPredicateImplGetPredicateTest() {
        OrPredicateImpl orPredicate = new OrPredicateImpl();
        ArrayList<Object> array = new ArrayList<>();
        Assert.assertEquals("Actual and expected values are not the same!", array, orPredicate.getPredicates());
    }

    @Test(expected = NullPointerException.class)
    public void orPredicateImplQueryPredicateId() {
        OrPredicateImpl orPredicateNull = new OrPredicateImpl(null);
        Assert.assertNull(orPredicateNull.getPredicates().get(0));
        QueryPredicate queryPredicate = new OrPredicateImpl();
        OrPredicateImpl orPredicate = new OrPredicateImpl(queryPredicate);
        Assert.assertEquals("Actual and expected values are not the same!", queryPredicate, orPredicate.getPredicates().get(0));
        QueryPredicate queryPredicate1 = new OrPredicateImpl();
        QueryPredicate queryPredicate2 = new OrPredicateImpl();
        QueryPredicate queryPredicate3 = new OrPredicateImpl();
        QueryPredicate[] array = {queryPredicate1, queryPredicate2, queryPredicate3};
        OrPredicateImpl orPredicateWithMultiplePredicates = new OrPredicateImpl(queryPredicate1, queryPredicate2, queryPredicate3);
        for (int i = 0; i < array.length; i++) {
            Assert.assertEquals("Actual and expected values are not the same!", array[i], orPredicateWithMultiplePredicates.getPredicates().get(i));
        }
    }

    @Test
    public void orTest() {
        OrPredicateImpl orPredicate = new OrPredicateImpl();
        QueryPredicate queryPredicate = new OrPredicateImpl();
        orPredicate.or(queryPredicate);
        Assert.assertEquals("Actual and expected values are not the same!", queryPredicate, orPredicate.getPredicates().get(0));
    }

    @Test
    public void orWithNullPredicateTest() {
        OrPredicateImpl orPredicate = new OrPredicateImpl();
        orPredicate.or(null);
        Assert.assertNull(orPredicate.getPredicates().get(0));
    }
}
