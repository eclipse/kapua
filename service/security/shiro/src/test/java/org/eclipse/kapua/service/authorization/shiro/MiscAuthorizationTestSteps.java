/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.shiro;

import javax.inject.Inject;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionFactoryImpl;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionImpl;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;

// Implementation of Gherkin steps used to test miscellaneous Shiro 
// authorization functionality.

@ScenarioScoped
public class MiscAuthorizationTestSteps extends AbstractAuthorizationServiceTest {

    // Test data scratchpads
    private CommonTestData commonData ;
    private MiscAuthorizationTestData miscData ;

    // Currently executing scenario.
    @SuppressWarnings("unused")
    private Scenario scenario;

    // Various Shiro Authorization related service references
    private PermissionFactory permissionFactory ;

    @Inject
    public MiscAuthorizationTestSteps(MiscAuthorizationTestData miscData, CommonTestData commonData) {
        this.miscData = miscData;
        this.commonData = commonData;
    }

    // Database setup and tear-down steps
    @Before
    public void beforeScenario(Scenario scenario) throws KapuaException {

        this.scenario = scenario;

        // Clean up the database. A clean slate is needed for truly independent
        // test case executions!
        dropDatabase();
        setupDatabase();

        permissionFactory = new PermissionFactoryImpl();

        // Clean up the test data scratchpads
        miscData.clearData();
        commonData.clearData();
    }

    // Cucumber test steps

    // The following test step is more of a filler. The only purpose is to achieve some coverage
    // of the Authorization Permission factory.
    // As such this step is of limited usefulness and should be taken with a grain of salt.
    @Then("^The permission factory returns sane results$")
    public void permissionFactorySanityChecks()
            throws KapuaException {

        Permission tmpPerm = null;
        Domain tmpDomain = new TestDomain();

        tmpPerm = permissionFactory.newPermission(tmpDomain, Actions.read, ROOT_SCOPE_ID);
        assertNotNull(tmpPerm);
        assertNotNull(tmpPerm.getDomain());
        assertEquals(tmpDomain.getName(), tmpPerm.getDomain());
        assertEquals(Actions.read, tmpPerm.getAction());

        tmpPerm = permissionFactory.newPermission(tmpDomain, Actions.write, ROOT_SCOPE_ID, generateId(9));
        assertNotNull(tmpPerm);
        assertNotNull(tmpPerm.getDomain());
        assertEquals(tmpDomain.getName(), tmpPerm.getDomain());
        assertEquals(Actions.write, tmpPerm.getAction());
        assertEquals(generateId(9), tmpPerm.getGroupId());
        assertFalse(tmpPerm.getForwardable());

        tmpPerm = permissionFactory.newPermission(null, Actions.execute, ROOT_SCOPE_ID, generateId(9), true);
        assertNotNull(tmpPerm);
        assertEquals(Actions.execute, tmpPerm.getAction());
        assertTrue(tmpPerm.getForwardable());

        tmpDomain.setName(null);
        tmpPerm = permissionFactory.newPermission(tmpDomain, Actions.connect, ROOT_SCOPE_ID, generateId());
        assertNotNull(tmpPerm);
        assertEquals(Actions.connect, tmpPerm.getAction());
    }

    // The following test step is more of a filler. The only purpose is to achieve some coverage
    // of the Authorization Permission object equals function.
    // As such this step is of limited usefulness and should be taken with a grain of salt.
    @Then("^I can compare permission objects$")
    public void checkPermissionComparison()
            throws KapuaException {

        Permission perm1 = new PermissionImpl("test_domain_1", Actions.read, generateId(10), generateId(100));
        Permission perm2 = new PermissionImpl("test_domain_1", Actions.read, generateId(10), generateId(100));

        assertTrue(perm1.equals(perm1));
        assertFalse(perm1.equals(null));
        assertFalse(perm1.equals(Integer.valueOf(10)));

        assertTrue(perm1.equals(perm2));

        perm1.setDomain(null);
        assertFalse(perm1.equals(perm2));
        perm2.setDomain(null);
        assertTrue(perm1.equals(perm2));
        perm1.setDomain("test_1");
        assertFalse(perm1.equals(perm2));
        perm2.setDomain("test_2");
        assertFalse(perm1.equals(perm2));

        perm1.setDomain("test");
        perm2.setDomain("test");

        perm1.setTargetScopeId(null);
        assertFalse(perm1.equals(perm2));
        perm2.setTargetScopeId(null);
        assertTrue(perm1.equals(perm2));
        perm1.setTargetScopeId(generateId(10));
        assertFalse(perm1.equals(perm2));
        perm2.setTargetScopeId(generateId(15));
        assertFalse(perm1.equals(perm2));

        perm1.setTargetScopeId(generateId(10));
        perm2.setTargetScopeId(generateId(10));

        perm1.setGroupId(null);
        assertFalse(perm1.equals(perm2));
        perm2.setGroupId(null);
        assertTrue(perm1.equals(perm2));
        perm1.setGroupId(generateId(100));
        assertFalse(perm1.equals(perm2));
        perm2.setGroupId(generateId(101));
        assertFalse(perm1.equals(perm2));
        perm2.setGroupId(generateId(100));
        assertTrue(perm1.equals(perm2));

        perm1.setAction(Actions.read);
        perm2.setAction(Actions.write);
        assertFalse(perm1.equals(perm2));
    }
}
