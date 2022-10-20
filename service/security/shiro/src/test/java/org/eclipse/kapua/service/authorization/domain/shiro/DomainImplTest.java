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
package org.eclipse.kapua.service.authorization.domain.shiro;

import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Category(JUnitTests.class)
public class DomainImplTest {

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
        Assert.assertNull("Null expected.", domainImpl1.getScopeId());
        Assert.assertNull("Null expected.", domainImpl1.getName());
        Assert.assertNull("Null expected.", domainImpl1.getActions());
        Assert.assertFalse("False expected.", domainImpl1.getGroupable());
    }

    @Test
    public void domainImplScopeIdParameterTest() {
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, domainImpl2.getScopeId());
        Assert.assertNull("Null expected.", domainImpl2.getName());
        Assert.assertNull("Null expected.", domainImpl2.getActions());
        Assert.assertFalse("False expected.", domainImpl2.getGroupable());
    }

    @Test
    public void domainImplNullScopeIdParameterTest() {
        DomainImpl domainImpl = new DomainImpl((KapuaId) null);
        Assert.assertNull("Null expected.", domainImpl.getScopeId());
        Assert.assertNull("Null expected.", domainImpl.getName());
        Assert.assertNull("Null expected.", domainImpl.getActions());
        Assert.assertFalse("False expected.", domainImpl.getGroupable());
    }

    @Test
    public void domainImplDomainParameterTest() {
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, domainImpl3.getId());
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ANY, domainImpl3.getScopeId());
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, domainImpl3.getCreatedBy());
        Assert.assertEquals("Expected and actual values should be the same.", createdOn, domainImpl3.getCreatedOn());
        Assert.assertEquals("Expected and actual values should be the same.", "name", domainImpl3.getName());
        Assert.assertEquals("Expected and actual values should be the same.", actions, domainImpl3.getActions());
        Assert.assertTrue("True expected.", domainImpl3.getGroupable());
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
            Assert.assertEquals("Expected and actual values should be the same.", newName, domainImpl1.getName());
            Assert.assertEquals("Expected and actual values should be the same.", newName, domainImpl2.getName());
            Assert.assertEquals("Expected and actual values should be the same.", newName, domainImpl3.getName());
        }

        domainImpl1.setName(null);
        domainImpl2.setName(null);
        domainImpl3.setName(null);
        Assert.assertNull("Null expected.", domainImpl1.getName());
        Assert.assertNull("Null expected.", domainImpl2.getName());
        Assert.assertNull("Null expected.", domainImpl3.getName());
    }

    @Test
    public void setAndGetActionsTest() {
        Set<Actions> newActions = new HashSet<>();

        domainImpl1.setActions(newActions);
        domainImpl2.setActions(newActions);
        domainImpl3.setActions(newActions);
        Assert.assertEquals("Expected and actual values should be the same.", newActions, domainImpl1.getActions());
        Assert.assertEquals("Expected and actual values should be the same.", newActions, domainImpl2.getActions());
        Assert.assertEquals("Expected and actual values should be the same.", newActions, domainImpl3.getActions());
        Assert.assertTrue("True expected", domainImpl1.getActions().isEmpty());
        Assert.assertTrue("True expected", domainImpl2.getActions().isEmpty());
        Assert.assertTrue("True expected", domainImpl3.getActions().isEmpty());

        newActions.add(Actions.read);
        newActions.add(Actions.delete);
        newActions.add(Actions.connect);
        newActions.add(Actions.execute);
        newActions.add(Actions.write);
        domainImpl1.setActions(newActions);
        domainImpl2.setActions(newActions);
        domainImpl3.setActions(newActions);

        Assert.assertEquals("Expected and actual values should be the same.", newActions, domainImpl1.getActions());
        Assert.assertEquals("Expected and actual values should be the same.", newActions, domainImpl2.getActions());
        Assert.assertEquals("Expected and actual values should be the same.", newActions, domainImpl3.getActions());
        Assert.assertEquals("Expected and actual values should be the same.", 5, domainImpl1.getActions().size());
        Assert.assertEquals("Expected and actual values should be the same.", 5, domainImpl2.getActions().size());
        Assert.assertEquals("Expected and actual values should be the same.", 5, domainImpl3.getActions().size());

        domainImpl1.setActions(null);
        domainImpl2.setActions(null);
        domainImpl3.setActions(null);
        Assert.assertNull("Null expected.", domainImpl1.getActions());
        Assert.assertNull("Null expected.", domainImpl2.getActions());
        Assert.assertNull("Null expected.", domainImpl3.getActions());
    }

    @Test
    public void setAndGetGroupableTest() {
        boolean[] groupables = {true, false};

        for (boolean groupable : groupables) {
            domainImpl1.setGroupable(groupable);
            domainImpl2.setGroupable(groupable);
            domainImpl3.setGroupable(groupable);

            Assert.assertEquals("Expected and actual values should be the same.", groupable, domainImpl1.getGroupable());
            Assert.assertEquals("Expected and actual values should be the same.", groupable, domainImpl2.getGroupable());
            Assert.assertEquals("Expected and actual values should be the same.", groupable, domainImpl3.getGroupable());
        }
    }

    @Test
    public void equalsSameObjectTest() {
        Assert.assertTrue("True expected.", domainImpl1.equals(domainImpl1));
    }

    @Test
    public void equalsNullTest() {
        Assert.assertFalse("False expected.", domainImpl1.equals(null));
    }

    @Test
    public void equalsObjectTest() {
        Assert.assertFalse("False expected.", domainImpl1.equals(new Object()));
    }

    @Test
    public void equalsTrueTest() {
        domainImpl1.setGroupable(true);
        domainImpl2.setGroupable(true);
        domainImpl1.setName("name");
        domainImpl2.setName("name");
        domainImpl1.setActions(actions);
        domainImpl2.setActions(actions);

        Assert.assertTrue("True expected.", domainImpl1.equals(domainImpl2));
    }

    @Test
    public void equalsDifferentGroupableTest() {
        domainImpl1.setGroupable(true);
        domainImpl2.setGroupable(false);
        domainImpl1.setName("name");
        domainImpl2.setName("name");
        domainImpl1.setActions(actions);
        domainImpl2.setActions(actions);

        Assert.assertFalse("False expected.", domainImpl1.equals(domainImpl2));
    }

    @Test
    public void equalsDifferentNamesTest() {
        domainImpl1.setGroupable(true);
        domainImpl2.setGroupable(true);
        domainImpl1.setName("name");
        domainImpl2.setName("different name");
        domainImpl1.setActions(actions);
        domainImpl2.setActions(actions);

        Assert.assertFalse("False expected.", domainImpl1.equals(domainImpl2));
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

        Assert.assertFalse("False expected.", domainImpl1.equals(domainImpl2));
    }

    @Test
    public void hashCodeTest() {
        Assert.assertEquals("Expected and actual values should be the same.", 31028, domainImpl1.hashCode());
    }

    @Test
    public void hashCodeNameTest() {
        domainImpl1.setName("name");
        Assert.assertEquals("Expected and actual values should be the same.", -1052803841, domainImpl1.hashCode());
    }

    @Test
    public void hashCodeEmptyActionsTest() {
        domainImpl1.setActions(actions);
        Assert.assertEquals("Expected and actual values should be the same.", 31028, domainImpl1.hashCode());
    }

    @Test
    public void hashCodeActionsTest() {
        actions.add(Actions.read);
        actions.add(Actions.delete);
        actions.add(Actions.connect);
        actions.add(Actions.execute);
        actions.add(Actions.write);
        domainImpl1.setActions(actions);

        Assert.assertEquals("Expected and actual values should be the same.", 31 * actions.hashCode() + 31028, domainImpl1.hashCode());
    }

    @Test
    public void hashCodeGroupableTrueTest() {
        domainImpl1.setGroupable(true);
        Assert.assertEquals("Expected and actual values should be the same.", 31022, domainImpl1.hashCode());
    }

    @Test
    public void hashCodeGroupableFalseTest() {
        domainImpl1.setGroupable(false);
        Assert.assertEquals("Expected and actual values should be the same.", 31028, domainImpl1.hashCode());
    }

    @Test
    public void hashCodeNameActionsGroupableTest() {
        domainImpl1.setName("name");
        domainImpl1.setActions(actions);
        domainImpl1.setGroupable(true);
        Assert.assertEquals("Expected and actual values should be the same.", -1052803847, domainImpl1.hashCode());
    }
}