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
import java.util.List;


@Category(JUnitTests.class)
public class AndPredicateImplTest {

    @Test
    public void andPredicateImpl() {
        AndPredicateImpl andPredicate = new AndPredicateImpl();
        ArrayList<Object> array = new ArrayList<>();
        Assert.assertEquals("Actual and expected values are not the same!", array, andPredicate.getPredicates());
    }

    @Test
    public void andPredicateImplQueryPredicateId() {
        QueryPredicate queryPredicate = new AndPredicateImpl();
        AndPredicateImpl andPredicate = new AndPredicateImpl(queryPredicate);
        Assert.assertEquals("Actual and expected values are not the same!", queryPredicate, andPredicate.getPredicates().get(0));
        QueryPredicate queryPredicate1 = new AndPredicateImpl();
        QueryPredicate queryPredicate2 = new AndPredicateImpl();
        QueryPredicate queryPredicate3 = new AndPredicateImpl();
        QueryPredicate[] queryPredicateArray = {queryPredicate1, queryPredicate2, queryPredicate3};
        AndPredicateImpl andPredicateWithMultiplePredicates = new AndPredicateImpl(queryPredicate1, queryPredicate2, queryPredicate3);
        for (int i = 0; i < queryPredicateArray.length; i++) {
            Assert.assertEquals("Actual and expected values are not the same!", queryPredicateArray[i], andPredicateWithMultiplePredicates.getPredicates().get(i));
        }
    }

    @Test
    public void andTest() {
        AndPredicateImpl andPredicate = new AndPredicateImpl();
        QueryPredicate queryPredicate = new AndPredicateImpl();
        andPredicate.and(queryPredicate);
        Assert.assertEquals("Actual and expected values are not the same!", queryPredicate, andPredicate.getPredicates().get(0));
    }

    @Test
    public void andWithNullPredicateTest() {
        AndPredicateImpl andPredicate = new AndPredicateImpl();
        andPredicate.and(null);
        Assert.assertNull(andPredicate.getPredicates().get(0));
    }

    @Test
    public void getPredicatesTest() {
        AndPredicateImpl andPredicate = new AndPredicateImpl();
        List<QueryPredicate> predicates = new ArrayList<>();
        predicates.add(new AndPredicateImpl());
        predicates.add(new AndPredicateImpl());
        predicates.add(new AndPredicateImpl());
        andPredicate.setPredicates(predicates);
        for (int i = 0; i < predicates.size(); i++) {
            Assert.assertEquals("Actual and expected values are not the same!", predicates.get(i), andPredicate.getPredicates().get(i));
        }
    }
}
