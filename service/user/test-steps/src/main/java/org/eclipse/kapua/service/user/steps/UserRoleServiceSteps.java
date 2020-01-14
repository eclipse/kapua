/*******************************************************************************
 * Copyright (c) 2011, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.user.steps;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.apache.shiro.SecurityUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.qa.common.TestDomain;
import org.eclipse.kapua.qa.common.TestJAXBContextProvider;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.service.authorization.access.AccessRoleService;
import org.eclipse.kapua.service.authorization.access.AccessRoleFactory;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessRoleCreator;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;


@ScenarioScoped
public class UserRoleServiceSteps extends TestBase {
    public static final Logger logger = LoggerFactory.getLogger(UserRoleServiceSteps.class);
    private static final TestDomain TEST_DOMAIN = new TestDomain();

    private UserService userService;
    private UserFactory userFactory;
    private AccessRoleService accessRoleService;
    private AccessRoleFactory accessRoleFactory;

    @Inject
    public UserRoleServiceSteps(StepData stepData, DBHelper dbHelper) {
        this.stepData = stepData;
        this.database = dbHelper;
    }

    @Before
    public void beforeScenario(Scenario scenario) {

        this.scenario = scenario;
        database.setup();
        stepData.clear();

        locator = KapuaLocator.getInstance();
        accessRoleService = locator.getService(AccessRoleService.class);
        userService = locator.getService(UserService.class);
        accessRoleFactory = locator.getFactory(AccessRoleFactory.class);
        userFactory = locator.getFactory(UserFactory.class);

        if (isUnitTest()) {
            // Create KapuaSession using KapuaSecurtiyUtils and kapua-sys user as logged in user.
            // All operations on database are performed using system user.
            // Only for unit tests. Integration tests assume that a real logon is performed.
            KapuaSession kapuaSession = new KapuaSession(null, SYS_SCOPE_ID, SYS_USER_ID);
            KapuaSecurityUtils.setSession(kapuaSession);
        }

        XmlUtil.setContextProvider(new TestJAXBContextProvider());
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

    @And("^I add access role \"([^\"]*)\" to user \"([^\"]*)\"$")
    public void addRoleToUser(String roleName, String userName) throws Exception {
        AccessInfo accessInfo = (AccessInfo) stepData.get("AccessInfo");
        Role role = (Role) stepData.get("Role");
        User user = (User) stepData.get("User");
        AccessRoleCreator accessRoleCreator = accessRoleFactory.newCreator(getCurrentScopeId());
            accessRoleCreator.setAccessInfoId(accessInfo.getId());
            accessRoleCreator.setRoleId(role.getId());
            stepData.put("AccessRoleCreator", accessRoleCreator);

            assertEquals(roleName, role.getName());
            assertEquals(userName, user.getName());

            try {
                primeException();
                stepData.remove("AccessRole");
                AccessRole accessRole = accessRoleService.create(accessRoleCreator);
                stepData.put("AccessRole", accessRole);
                stepData.put("AccessRoleId", accessRole.getId());
            } catch (KapuaException ex) {
                verifyException(ex);
            }
    }

    @Then("^Access role is not found$")
    public void accessRoleIsNotFound() throws Exception{
        AccessRole accessRole = (AccessRole) stepData.get("AccessRole");

        try {
            assertEquals(null, accessRoleService.find(getCurrentScopeId(), accessRole.getId()));
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I add access role \"([^\"]*)\" to created users$")
    public void iAddAccessRoleToUsers(String roleName) throws Exception {

        ArrayList<AccessInfo> accessInfoList = (ArrayList<AccessInfo>) stepData.get("AccessInfoList");
        ArrayList<AccessRole> accessRoleList = new ArrayList<>();
        Role role = (Role) stepData.get("Role");
        AccessRoleCreator accessRoleCreator = accessRoleFactory.newCreator(getCurrentScopeId());
        accessRoleCreator.setRoleId(role.getId());
        stepData.put("AccessRoleCreator", accessRoleCreator);
        assertEquals(roleName, role.getName());

        for (AccessInfo accessInfo : accessInfoList) {
            accessRoleCreator.setAccessInfoId(accessInfo.getId());
            try {
                primeException();
                stepData.remove("AccessRole");
                AccessRole accessRole = accessRoleService.create(accessRoleCreator);
                stepData.put("AccessRole", accessRole);
                stepData.put("AccessRoleId", accessRole.getId());
                accessRoleList.add(accessRole);
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }
        stepData.put("AccessRoleList", accessRoleList);
    }
}
