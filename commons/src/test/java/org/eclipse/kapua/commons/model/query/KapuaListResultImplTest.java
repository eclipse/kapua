/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.model.query;

import org.eclipse.kapua.commons.model.AbstractKapuaEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.util.RandomUtils;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;


@Category(JUnitTests.class)
public class KapuaListResultImplTest extends Assert {

    private final static Random RANDOM = RandomUtils.getInstance();

    KapuaListResultImpl<AbstractKapuaEntity> kapuaListResult = new KapuaListResultImpl<>();

    @Test
    public void kapuaListResultImplTest() {
        assertFalse(kapuaListResult.isLimitExceeded());
    }

    @Test
    public void setTrueAndFalseLimitExceededTest() {
        kapuaListResult.setLimitExceeded(true);
        assertTrue(kapuaListResult.isLimitExceeded());
        kapuaListResult.setLimitExceeded(false);
        assertFalse(kapuaListResult.isLimitExceeded());
    }

    @Test
    public void getItemTest() {
        AbstractKapuaEntity kapuaEntity = Mockito.mock(AbstractKapuaEntity.class);
        kapuaListResult.addItem(kapuaEntity);
        assertEquals("Actual and expected values are not the same!", kapuaEntity, kapuaListResult.getItem(0));
    }

    @Test
    public void getFirstItemTest() {
        AbstractKapuaEntity kapuaEntity = Mockito.mock(AbstractKapuaEntity.class);
        kapuaListResult.addItem(kapuaEntity);
        assertEquals("Actual and expected values are not the same!", kapuaEntity, kapuaListResult.getFirstItem());
    }

    @Test
    public void getSizeTest() {
        assertEquals("Actual and expected values are not the same!", 0, kapuaListResult.getSize());
        List<AbstractKapuaEntity> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            items.add(Mockito.mock(AbstractKapuaEntity.class));
        }
        kapuaListResult.addItems(items);
        assertEquals("Actual and expected values are not the same!", 10, kapuaListResult.getSize());
    }

    @Test
    public void isEmptyTest() {
        assertTrue(kapuaListResult.isEmpty());
        AbstractKapuaEntity kapuaEntity = Mockito.mock(AbstractKapuaEntity.class);
        kapuaListResult.addItem(kapuaEntity);
        assertFalse(kapuaListResult.isEmpty());
    }

    @Test
    public void getItemsTest() {
        List<AbstractKapuaEntity> items = new ArrayList<>();
        assertEquals("Actual and expected values are not the same!", items, kapuaListResult.getItems());
    }

    @Test
    public void addItemsTest() {
        List<AbstractKapuaEntity> items = new ArrayList<>();
        kapuaListResult.addItems(items);
        assertEquals("Actual and expected values are not the same!", items, kapuaListResult.getItems());
    }

    @Test
    public void addItemTest() {
        AbstractKapuaEntity kapuaEntityFirst = Mockito.mock(AbstractKapuaEntity.class);
        kapuaListResult.addItem(kapuaEntityFirst);
        assertEquals("Actual and expected values are not the same!", kapuaEntityFirst, kapuaListResult.getFirstItem());
        AbstractKapuaEntity kapuaEntitySecond = Mockito.mock(AbstractKapuaEntity.class);
        kapuaListResult.addItem(kapuaEntitySecond);
        AbstractKapuaEntity kapuaEntityThird = Mockito.mock(AbstractKapuaEntity.class);
        kapuaListResult.addItem(kapuaEntityThird);
        for (AbstractKapuaEntity item : kapuaListResult.getItems()) {
            assertTrue(kapuaListResult.getItems().contains(item));
        }
    }

    @Test
    public void clearItemsTest() {
        List<AbstractKapuaEntity> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            items.add(Mockito.mock(AbstractKapuaEntity.class));
        }
        kapuaListResult.addItems(items);
        for (int i = 0; i < 10; i++) {
            assertEquals("Actual and expected values are not the same!", items.get(i), kapuaListResult.getItems().get(i));
        }
        kapuaListResult.clearItems();
        assertEquals("Actual and expected values are not the same!", 0, kapuaListResult.getSize());
    }

    @Test
    public void sortTest() {
        List<AbstractKapuaEntity> items = new ArrayList<>();
        BigInteger eid1 = new BigInteger(64, RANDOM);
        BigInteger eid2 = new BigInteger(64, RANDOM);
        BigInteger eid3 = new BigInteger(64, RANDOM);

        AbstractKapuaEntity item1 = Mockito.mock(AbstractKapuaEntity.class);
        Mockito.when(item1.getScopeId()).thenReturn(new KapuaEid(eid1));
        items.add(item1);

        AbstractKapuaEntity item2 = Mockito.mock(AbstractKapuaEntity.class);
        Mockito.when(item2.getScopeId()).thenReturn(new KapuaEid(eid2));
        items.add(item2);

        AbstractKapuaEntity item3 = Mockito.mock(AbstractKapuaEntity.class);
        Mockito.when(item3.getScopeId()).thenReturn(new KapuaEid(eid3));
        items.add(item3);

        kapuaListResult.addItem(items.get(0));
        kapuaListResult.addItem(items.get(1));
        kapuaListResult.addItem(items.get(2));

        kapuaListResult.sort(new SortByHashCode());
        items.sort(new SortByHashCode());

        for (int i = 0; i < items.size(); i++) {
            assertEquals("Actual and expected values are not the same!", items.get(i), kapuaListResult.getItem(i));
        }
    }

    @Test
    public void getTotalCountTest() {
        Long totalCount = null;
        kapuaListResult.setTotalCount(totalCount);
        assertNull(kapuaListResult.getTotalCount());
        Long totalCountMax = Long.MAX_VALUE;
        kapuaListResult.setTotalCount(totalCountMax);
        assertEquals("Actual and expected values are not the same!", totalCountMax, kapuaListResult.getTotalCount());
        Long totalCountMin = Long.MIN_VALUE;
        kapuaListResult.setTotalCount(totalCountMin);
        assertEquals("Actual and expected values are not the same!", totalCountMin, kapuaListResult.getTotalCount());
    }
}

class SortByHashCode implements Comparator<AbstractKapuaEntity> {

    public int compare(AbstractKapuaEntity a, AbstractKapuaEntity b) {
        return (a.getScopeId().hashCode() - b.getScopeId().hashCode());
    }
}