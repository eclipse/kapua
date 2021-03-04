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
package org.eclipse.kapua.service.authorization.domain.shiro;

import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.Categories;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Category(Categories.junitTests.class)
public class DomainImplTest extends Assert {

    Domain domain;
    Date createdOn;
    Set<Actions> actions;
    DomainImpl domainImpl1, domainImpl2, domainImpl3;

    @Before
    public void initialize() {
        domain = Mockito.mock(Domain.class);
        createdOn = new Date();
        actions = new HashSet<>();

        Mockito.when(domain.getId()).thenReturn(KapuaId.ONE);
        Mockito.when(domain.getScopeId()).thenReturn(KapuaId.ANY);
        Mockito.when(domain.getCreatedBy()).thenReturn(KapuaId.ONE);
        Mockito.when(domain.getCreatedOn()).thenReturn(createdOn);
        Mockito.when(domain.getName()).thenReturn("name");
        Mockito.when(domain.getActions()).thenReturn(actions);
        Mockito.when(domain.getGroupable()).thenReturn(true);

        domainImpl1 = new DomainImpl();
        domainImpl2 = new DomainImpl(KapuaId.ONE);
        domainImpl3 = new DomainImpl(domain);
    }

    @Test
    public void domainImplWithoutParametersTest() {
        assertNull("Null expected.", domainImpl1.getScopeId());
        assertNull("Null expected.", domainImpl1.getName());
        assertNull("Null expected.", domainImpl1.getActions());
        assertFalse("False expected.", domainImpl1.getGroupable());
    }

    @Test
    public void domainImplScopeIdParameterTest() {
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, domainImpl2.getScopeId());
        assertNull("Null expected.", domainImpl2.getName());
        assertNull("Null expected.", domainImpl2.getActions());
        assertFalse("False expected.", domainImpl2.getGroupable());
    }

    @Test
    public void domainImplNullScopeIdParameterTest() {
        DomainImpl domainImpl = new DomainImpl((KapuaId) null);
        assertNull("Null expected.", domainImpl.getScopeId());
        assertNull("Null expected.", domainImpl.getName());
        assertNull("Null expected.", domainImpl.getActions());
        assertFalse("False expected.", domainImpl.getGroupable());
    }

    @Test
    public void domainImplDomainParameterTest() {
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, domainImpl3.getId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, domainImpl3.getScopeId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, domainImpl3.getCreatedBy());
        assertEquals("Expected and actual values should be the same.", createdOn, domainImpl3.getCreatedOn());
        assertEquals("Expected and actual values should be the same.", "name", domainImpl3.getName());
        assertEquals("Expected and actual values should be the same.", actions, domainImpl3.getActions());
        assertTrue("True expected.", domainImpl3.getGroupable());
    }

    @Test(expected = NullPointerException.class)
    public void domainImplNullDomainParameterTest() {
        new DomainImpl((Domain) null);
    }

    @Test
    public void setAndGetName() {
        String[] newNames = {"", "  na123)(&*^&NAME NEWname  <>", "newNa-,,..,,Me name NEW NEW---", "-&^454536 na___,,12 NAME New    name    ", "! 2#@ na     meNewNAME 23NeW23", "12&^%4   ,,,. '|<new name>*(", "       ,,123name;;'", "12#name-new-name765   ,.aaa!!#$%^<> "};

        for (String newName : newNames) {
            domainImpl1.setName(newName);
            domainImpl2.setName(newName);
            domainImpl3.setName(newName);
            assertEquals("Expected and actual values should be the same.", newName, domainImpl1.getName());
            assertEquals("Expected and actual values should be the same.", newName, domainImpl2.getName());
            assertEquals("Expected and actual values should be the same.", newName, domainImpl3.getName());
        }

        domainImpl1.setName(null);
        domainImpl2.setName(null);
        domainImpl3.setName(null);
        assertNull("Null expected.", domainImpl1.getName());
        assertNull("Null expected.", domainImpl2.getName());
        assertNull("Null expected.", domainImpl3.getName());
    }

    @Test
    public void setAndGetActionsTest() {
        Set<Actions> newActions = new HashSet<>();

        domainImpl1.setActions(newActions);
        domainImpl2.setActions(newActions);
        domainImpl3.setActions(newActions);
        assertEquals("Expected and actual values should be the same.", newActions, domainImpl1.getActions());
        assertEquals("Expected and actual values should be the same.", newActions, domainImpl2.getActions());
        assertEquals("Expected and actual values should be the same.", newActions, domainImpl3.getActions());
        assertTrue("True expected", domainImpl1.getActions().isEmpty());
        assertTrue("True expected", domainImpl2.getActions().isEmpty());
        assertTrue("True expected", domainImpl3.getActions().isEmpty());

        newActions.add(Actions.read);
        newActions.add(Actions.delete);
        newActions.add(Actions.connect);
        newActions.add(Actions.execute);
        newActions.add(Actions.write);
        domainImpl1.setActions(newActions);
        domainImpl2.setActions(newActions);
        domainImpl3.setActions(newActions);

        assertEquals("Expected and actual values should be the same.", newActions, domainImpl1.getActions());
        assertEquals("Expected and actual values should be the same.", newActions, domainImpl2.getActions());
        assertEquals("Expected and actual values should be the same.", newActions, domainImpl3.getActions());
        assertEquals("Expected and actual values should be the same.", 5, domainImpl1.getActions().size());
        assertEquals("Expected and actual values should be the same.", 5, domainImpl2.getActions().size());
        assertEquals("Expected and actual values should be the same.", 5, domainImpl3.getActions().size());

        domainImpl1.setActions(null);
        domainImpl2.setActions(null);
        domainImpl3.setActions(null);
        assertNull("Null expected.", domainImpl1.getActions());
        assertNull("Null expected.", domainImpl2.getActions());
        assertNull("Null expected.", domainImpl3.getActions());
    }

    @Test
    public void setAndGetGroupableTest() {
        boolean[] groupables = {true, false};

        for (boolean groupable : groupables) {
            domainImpl1.setGroupable(groupable);
            domainImpl2.setGroupable(groupable);
            domainImpl3.setGroupable(groupable);

            assertEquals("Expected and actual values should be the same.", groupable, domainImpl1.getGroupable());
            assertEquals("Expected and actual values should be the same.", groupable, domainImpl2.getGroupable());
            assertEquals("Expected and actual values should be the same.", groupable, domainImpl3.getGroupable());
        }
    }

    @Test
    public void equalsSameObjectTest() {
        assertTrue("True expected.", domainImpl1.equals(domainImpl1));
    }

    @Test
    public void equalsNullTest() {
        assertFalse("False expected.", domainImpl1.equals(null));
    }

    @Test
    public void equalsObjectTest() {
        assertFalse("False expected.", domainImpl1.equals(new Object()));
    }

    @Test
    public void equalsTrueTest() {
        domainImpl1.setGroupable(true);
        domainImpl2.setGroupable(true);
        domainImpl1.setName("name");
        domainImpl2.setName("name");
        domainImpl1.setActions(actions);
        domainImpl2.setActions(actions);

        assertTrue("True expected.", domainImpl1.equals(domainImpl2));
    }

    @Test
    public void equalsDifferentGroupableTest() {
        domainImpl1.setGroupable(true);
        domainImpl2.setGroupable(false);
        domainImpl1.setName("name");
        domainImpl2.setName("name");
        domainImpl1.setActions(actions);
        domainImpl2.setActions(actions);

        assertFalse("False expected.", domainImpl1.equals(domainImpl2));
    }

    @Test
    public void equalsDifferentNamesTest() {
        domainImpl1.setGroupable(true);
        domainImpl2.setGroupable(true);
        domainImpl1.setName("name");
        domainImpl2.setName("different name");
        domainImpl1.setActions(actions);
        domainImpl2.setActions(actions);

        assertFalse("False expected.", domainImpl1.equals(domainImpl2));
    }

    @Test
    public void equalsDifferentActionsTest() {
        domainImpl1.setGroupable(true);
        domainImpl2.setGroupable(true);
        domainImpl1.setName("name");
        domainImpl2.setName("name");
        actions.add(Actions.delete);
        domainImpl1.setActions(actions);
        domainImpl2.setActions(new HashSet<>());

        assertFalse("False expected.", domainImpl1.equals(domainImpl2));
    }

    @Test
    public void hashCodeTest() {
        assertEquals("Expected and actual values should be the same.", 31028, domainImpl1.hashCode());
    }

    @Test
    public void hashCodeNameTest() {
        domainImpl1.setName("name");
        assertEquals("Expected and actual values should be the same.", -1052803841, domainImpl1.hashCode());
    }

    @Test
    public void hashCodeEmptyActionsTest() {
        domainImpl1.setActions(actions);
        assertEquals("Expected and actual values should be the same.", 31028, domainImpl1.hashCode());
    }

    @Test
    public void hashCodeActionsTest() {
        actions.add(Actions.read);
        actions.add(Actions.delete);
        actions.add(Actions.connect);
        actions.add(Actions.execute);
        actions.add(Actions.write);
        domainImpl1.setActions(actions);

        assertEquals("Expected and actual values should be the same.", 31 * actions.hashCode() + 31028, domainImpl1.hashCode());
    }

    @Test
    public void hashCodeGroupableTrueTest() {
        domainImpl1.setGroupable(true);
        assertEquals("Expected and actual values should be the same.", 31022, domainImpl1.hashCode());
    }

    @Test
    public void hashCodeGroupableFalseTest() {
        domainImpl1.setGroupable(false);
        assertEquals("Expected and actual values should be the same.", 31028, domainImpl1.hashCode());
    }

    @Test
    public void hashCodeNameActionsGroupableTest() {
        domainImpl1.setName("name");
        domainImpl1.setActions(actions);
        domainImpl1.setGroupable(true);
        assertEquals("Expected and actual values should be the same.", -1052803847, domainImpl1.hashCode());
    }
}