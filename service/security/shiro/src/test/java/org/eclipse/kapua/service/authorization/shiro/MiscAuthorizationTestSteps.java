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
 *
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;

// Implementation of Gherkin steps used to test miscellaneous Shiro 
// authorization functionality.

@ScenarioScoped
public class MiscAuthorizationTestSteps extends AbstractAuthorizationServiceTest {

    @SuppressWarnings("unused")
    private static final Logger s_logger = LoggerFactory.getLogger(AccessInfoServiceTestSteps.class);

    // Test data scratchpads
    private CommonTestData commonData = null;
    private MiscAuthorizationTestData miscData = null;

    // Currently executing scenario.
    @SuppressWarnings("unused")
    private Scenario scenario;
    
    // Various Shiro Authorization related service references
    private PermissionFactory permissionFactory = null;
    
    
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
        
        tmpPerm = permissionFactory.newPermission(tmpDomain, Actions.read, rootScopeId);
        assertNotNull(tmpPerm);
        assertNotNull(tmpPerm.getDomain());
        assertEquals(tmpDomain.getName(), tmpPerm.getDomain());
        assertEquals(Actions.read, tmpPerm.getAction());
        
        tmpPerm = permissionFactory.newPermission(tmpDomain, Actions.write, rootScopeId, generateId());
        assertNotNull(tmpPerm);
        assertNotNull(tmpPerm.getDomain());
        assertEquals(tmpDomain.getName(), tmpPerm.getDomain());
        assertEquals(Actions.write, tmpPerm.getAction());
        
        tmpPerm = permissionFactory.newPermission(null, Actions.execute, rootScopeId, generateId());
        assertNotNull(tmpPerm);
        assertEquals(Actions.execute, tmpPerm.getAction());
        
        tmpDomain.setName(null);
        tmpPerm = permissionFactory.newPermission(tmpDomain, Actions.connect, rootScopeId, generateId());
        assertNotNull(tmpPerm);
        assertEquals(Actions.connect, tmpPerm.getAction());
        
        tmpPerm = permissionFactory.parseString("test_domain_1:read:1:15");
        assertNotNull(tmpPerm);
        assertNotNull(tmpPerm.getDomain());
        assertEquals("test_domain_1", tmpPerm.getDomain());
        assertEquals(Actions.read, tmpPerm.getAction());
        assertNotNull(tmpPerm.getTargetScopeId());
        assertEquals(rootScopeId, tmpPerm.getTargetScopeId());
        assertNotNull(tmpPerm.getGroupId());
        assertEquals(generateId(15), tmpPerm.getGroupId());

        tmpPerm = permissionFactory.parseString("test_domain_1:*:1:15");
        assertNotNull(tmpPerm);

        tmpPerm = permissionFactory.parseString("test_domain_1:*:*:*");
        assertNotNull(tmpPerm);
        
        tmpPerm = permissionFactory.parseString("test_domain_1");
        assertNotNull(tmpPerm);
        
        tmpPerm = permissionFactory.parseString("test_domain_1:execute");
        assertNotNull(tmpPerm);
        
        tmpPerm = permissionFactory.parseString("test_domain_1:execute:10");
        assertNotNull(tmpPerm);
        
        try {
            commonData.exceptionCaught = false;
            tmpPerm = permissionFactory.parseString("");
        } catch (KapuaException ex) {
            commonData.exceptionCaught = true;
        }
        assertTrue(commonData.exceptionCaught);

        try {
            commonData.exceptionCaught = false;
            tmpPerm = permissionFactory.parseString("test_domain_1:read:1:15:wrong");
        } catch (KapuaException ex) {
            commonData.exceptionCaught = true;
        }
        assertTrue(commonData.exceptionCaught);
        
        try {
            commonData.exceptionCaught = false;
            tmpPerm = permissionFactory.parseString("test_domain_1:garble");
        } catch (Exception ex) {
            commonData.exceptionCaught = true;
        }
        assertTrue(commonData.exceptionCaught);

        try {
            commonData.exceptionCaught = false;
            tmpPerm = permissionFactory.parseString("test_domain_1:read:wrong");
        } catch (Exception ex) {
            commonData.exceptionCaught = true;
        }
        assertTrue(commonData.exceptionCaught);

        try {
            commonData.exceptionCaught = false;
            tmpPerm = permissionFactory.parseString("test_domain_1:read:1:wrong");
        } catch (Exception ex) {
            commonData.exceptionCaught = true;
        }
        assertTrue(commonData.exceptionCaught);
    }

    // The following test step is more of a filler. The only purpose is to achieve some coverage
    // of the Authorization Permission object equals function.
    // As such this step is of limited usefulness and should be taken with a grain of salt.
    @Then("^I can compare permission objects$")
    public void checkPermissionComparison()
            throws KapuaException {
        
        Permission perm_1 = new PermissionImpl("test_domain_1", Actions.read, generateId(10), generateId(100));
        Permission perm_2 = new PermissionImpl("test_domain_1", Actions.read, generateId(10), generateId(100));
        
        assertTrue(perm_1.equals(perm_1));
        assertFalse(perm_1.equals(null));
        assertFalse(perm_1.equals(new Integer(10)));
        
        assertTrue(perm_1.equals(perm_2));
        
        perm_1.setDomain(null);
        assertFalse(perm_1.equals(perm_2));
        perm_2.setDomain(null);
        assertTrue(perm_1.equals(perm_2));
        perm_1.setDomain("test_1");
        assertFalse(perm_1.equals(perm_2));
        perm_2.setDomain("test_2");
        assertFalse(perm_1.equals(perm_2));
        
        perm_1.setDomain("test");
        perm_2.setDomain("test");
        
        perm_1.setTargetScopeId(null);
        assertFalse(perm_1.equals(perm_2));
        perm_2.setTargetScopeId(null);
        assertTrue(perm_1.equals(perm_2));
        perm_1.setTargetScopeId(generateId(10));
        assertFalse(perm_1.equals(perm_2));
        perm_2.setTargetScopeId(generateId(15));
        assertFalse(perm_1.equals(perm_2));
        
        perm_1.setTargetScopeId(generateId(10));
        perm_2.setTargetScopeId(generateId(10));

        perm_1.setGroupId(null);
        assertFalse(perm_1.equals(perm_2));
        perm_2.setGroupId(null);
        assertTrue(perm_1.equals(perm_2));
        perm_1.setGroupId(generateId(100));
        assertFalse(perm_1.equals(perm_2));
        perm_2.setGroupId(generateId(101));
        assertFalse(perm_1.equals(perm_2));
        perm_2.setGroupId(generateId(100));
        assertTrue(perm_1.equals(perm_2));

        perm_1.setAction(Actions.read);
        perm_2.setAction(Actions.write);
        assertFalse(perm_1.equals(perm_2));
    }
}
