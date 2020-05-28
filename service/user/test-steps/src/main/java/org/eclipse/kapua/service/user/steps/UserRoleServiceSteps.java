/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.user.steps;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.service.authorization.access.AccessRoleService;
import org.eclipse.kapua.service.authorization.access.AccessRoleFactory;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessRoleCreator;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.user.User;

import com.google.inject.Singleton;

import javax.inject.Inject;
import java.util.ArrayList;

@Singleton
public class UserRoleServiceSteps extends TestBase {

    private AccessRoleService accessRoleService;
    private AccessRoleFactory accessRoleFactory;

    private static final String ACCESS_ROLE = "AccessRole";

    @Inject
    public UserRoleServiceSteps(StepData stepData) {
        super(stepData);
    }

    @After(value="@setup")
    public void setServices() {
        KapuaLocator locator = KapuaLocator.getInstance();
        accessRoleService = locator.getService(AccessRoleService.class);
        accessRoleFactory = locator.getFactory(AccessRoleFactory.class);
    }

    @Before
    public void beforeScenarioDockerFull(Scenario scenario) {
        updateScenario(scenario);
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
                stepData.remove(ACCESS_ROLE);
                AccessRole accessRole = accessRoleService.create(accessRoleCreator);
                stepData.put(ACCESS_ROLE, accessRole);
                stepData.put("AccessRoleId", accessRole.getId());
            } catch (KapuaException ex) {
                verifyException(ex);
            }
    }

    @Then("^Access role is not found$")
    public void accessRoleIsNotFound() throws Exception{
        AccessRole accessRole = (AccessRole) stepData.get(ACCESS_ROLE);

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
                stepData.remove(ACCESS_ROLE);
                AccessRole accessRole = accessRoleService.create(accessRoleCreator);
                stepData.put(ACCESS_ROLE, accessRole);
                stepData.put("AccessRoleId", accessRole.getId());
                accessRoleList.add(accessRole);
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }
        stepData.put("AccessRoleList", accessRoleList);
    }
}
