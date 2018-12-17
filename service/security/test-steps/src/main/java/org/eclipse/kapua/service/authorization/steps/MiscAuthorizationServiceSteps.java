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
package org.eclipse.kapua.service.authorization.steps;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.apache.shiro.SecurityUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.metatype.KapuaMetatypeFactoryImpl;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.service.authorization.AuthorizationJAXBContextProvider;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionFactoryImpl;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionImpl;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;
import org.eclipse.kapua.test.MockedLocator;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

// Implementation of Gherkin steps used to test miscellaneous Shiro 
// authorization functionality.

@ScenarioScoped
public class MiscAuthorizationServiceSteps extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(MiscAuthorizationServiceSteps.class);

    // Various Shiro Authorization related service references
    private PermissionFactory permissionFactory;

    @Inject
    public MiscAuthorizationServiceSteps(StepData stepData, DBHelper dbHelper) {

        this.stepData = stepData;
        this.database = dbHelper;
    }

    /**
     * Setup DI with Google Guice DI.
     * Create mocked and non mocked service under test and bind them with Guice.
     * It is based on custom MockedLocator locator that is meant for sevice unit tests.
     */
    private void setupDI() {

        MockedLocator mockedLocator = (MockedLocator) KapuaLocator.getInstance();

        AbstractModule module = new AbstractModule() {

            @Override
            protected void configure() {

                // Inject mocked Authorization Service method checkPermission
                AuthorizationService mockedAuthorization = Mockito.mock(AuthorizationService.class);
                try {
                    Mockito.doNothing().when(mockedAuthorization).checkPermission(Matchers.any(Permission.class));
                } catch (KapuaException e) {
                    // skip
                }
                bind(AuthorizationService.class).toInstance(mockedAuthorization);
                // Inject mocked Permission Factory
                bind(PermissionFactory.class).toInstance(Mockito.mock(PermissionFactory.class));
                // Set KapuaMetatypeFactory for Metatype configuration
                bind(KapuaMetatypeFactory.class).toInstance(new KapuaMetatypeFactoryImpl());

                // Inject actual account related services
                bind(AuthorizationEntityManagerFactory.class).toInstance(AuthorizationEntityManagerFactory.getInstance());
                bind(PermissionFactory.class).toInstance(new PermissionFactoryImpl());
            }
        };

        Injector injector = Guice.createInjector(module);
        mockedLocator.setInjector(injector);
    }

    // Database setup and tear-down steps
    @Before
    public void beforeScenario(Scenario scenario) throws KapuaException {

        if (isUnitTest()) {
            setupDI();
        }

        this.scenario = scenario;
        database.setup();
        stepData.clear();

        locator = KapuaLocator.getInstance();
        permissionFactory = locator.getFactory(PermissionFactory.class);

        if (isUnitTest()) {
            // Create KapuaSession using KapuaSecurtiyUtils and kapua-sys user as logged in user.
            // All operations on database are performed using system user.
            // Only for unit tests. Integration tests assume that a real logon is performed.
            KapuaSession kapuaSession = new KapuaSession(null, SYS_SCOPE_ID, SYS_USER_ID);
            KapuaSecurityUtils.setSession(kapuaSession);
        }

        XmlUtil.setContextProvider(new AuthorizationJAXBContextProvider());
    }

    @After
    public void afterScenario() {

        // Clean up the database
        try {
            logger.info("Logging out in cleanup");
            if (isIntegrationTest()) {
                database.deleteAll();
                SecurityUtils.getSubject().logout();
            } else {
                database.dropAll();
                database.close();
            }
            KapuaSecurityUtils.clearSession();
        } catch (Exception e) {
            logger.error("Failed to log out in @After", e);
        }
    }

    // Cucumber test steps

    // The following test step is more of a filler. The only purpose is to achieve some coverage
    // of the Authorization Permission factory.
    // As such this step is of limited usefulness and should be taken with a grain of salt.
    @Then("^The permission factory returns sane results$")
    public void permissionFactorySanityChecks()
            throws KapuaException {

        Permission tmpPerm = null;
        TestDomain tmpDomain = new TestDomain();

        tmpPerm = permissionFactory.newPermission(tmpDomain, Actions.read, SYS_SCOPE_ID);
        assertNotNull(tmpPerm);
        assertNotNull(tmpPerm.getDomain());
        assertEquals(tmpDomain.getName(), tmpPerm.getDomain());
        assertEquals(Actions.read, tmpPerm.getAction());

        tmpPerm = permissionFactory.newPermission(tmpDomain, Actions.write, SYS_SCOPE_ID, getKapuaId(9));
        assertNotNull(tmpPerm);
        assertNotNull(tmpPerm.getDomain());
        assertEquals(tmpDomain.getName(), tmpPerm.getDomain());
        assertEquals(Actions.write, tmpPerm.getAction());
        assertEquals(getKapuaId(9), tmpPerm.getGroupId());
        assertFalse(tmpPerm.getForwardable());

        tmpPerm = permissionFactory.newPermission(null, Actions.execute, SYS_SCOPE_ID, getKapuaId(9), true);
        assertNotNull(tmpPerm);
        assertEquals(Actions.execute, tmpPerm.getAction());
        assertTrue(tmpPerm.getForwardable());

        tmpDomain.setName(null);
        tmpPerm = permissionFactory.newPermission(tmpDomain, Actions.connect, SYS_SCOPE_ID, getKapuaId());
        assertNotNull(tmpPerm);
        assertEquals(Actions.connect, tmpPerm.getAction());
    }

    // The following test step is more of a filler. The only purpose is to achieve some coverage
    // of the Authorization Permission object equals function.
    // As such this step is of limited usefulness and should be taken with a grain of salt.
    @Then("^I can compare permission objects$")
    public void checkPermissionComparison() {

        Permission perm1 = new PermissionImpl("test_domain_1", Actions.read, getKapuaId(10), getKapuaId(100));
        Permission perm2 = new PermissionImpl("test_domain_1", Actions.read, getKapuaId(10), getKapuaId(100));

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
        perm1.setTargetScopeId(getKapuaId(10));
        assertFalse(perm1.equals(perm2));
        perm2.setTargetScopeId(getKapuaId(15));
        assertFalse(perm1.equals(perm2));

        perm1.setTargetScopeId(getKapuaId(10));
        perm2.setTargetScopeId(getKapuaId(10));

        perm1.setGroupId(null);
        assertFalse(perm1.equals(perm2));
        perm2.setGroupId(null);
        assertTrue(perm1.equals(perm2));
        perm1.setGroupId(getKapuaId(100));
        assertFalse(perm1.equals(perm2));
        perm2.setGroupId(getKapuaId(101));
        assertFalse(perm1.equals(perm2));
        perm2.setGroupId(getKapuaId(100));
        assertTrue(perm1.equals(perm2));

        perm1.setAction(Actions.read);
        perm2.setAction(Actions.write);
        assertFalse(perm1.equals(perm2));
    }
}
