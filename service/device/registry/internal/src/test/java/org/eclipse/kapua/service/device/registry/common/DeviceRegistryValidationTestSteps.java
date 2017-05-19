/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.common;

import static org.eclipse.kapua.service.device.registry.DeviceCredentialsMode.LOOSE;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import java.math.BigInteger;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.metatype.KapuaMetatypeFactoryImpl;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.id.KapuaIdFactoryImpl;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.locator.guice.KapuaLocatorImpl;
import org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdFactory;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceQuery;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.DeviceStatus;
import org.eclipse.kapua.service.device.registry.internal.DeviceCreatorImpl;
import org.eclipse.kapua.service.device.registry.internal.DeviceFactoryImpl;
import org.eclipse.kapua.service.device.registry.internal.DeviceImpl;
import org.eclipse.kapua.service.device.registry.internal.DeviceRegistryServiceImpl;
import org.eclipse.kapua.test.MockedLocator;
import org.eclipse.kapua.test.steps.AbstractKapuaSteps;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

/**
 * Implementation of Gherkin steps used in DeviceRegistryValidation.feature scenarios.
 * <p>
 * MockedLocator is used for Location Service. Mockito is used to mock other
 * services that the Device Registry services dependent on. Dependent services are: -
 * Authorization Service.
 */
@ScenarioScoped
public class DeviceRegistryValidationTestSteps extends AbstractKapuaSteps {

    KapuaId rootScopeId = new KapuaEid(BigInteger.ONE);

    @SuppressWarnings("unused")
    private static final Logger s_logger = LoggerFactory.getLogger(DeviceRegistryValidationTestSteps.class);

    // Currently executing scenario.
    Scenario scenario;

    // Device validator object
    // DeviceValidation deviceValidator = null;

    // Commons related objects
    KapuaIdFactory kapuaIdFactory = new KapuaIdFactoryImpl();

    // Device registry related objects
    DeviceRegistryService deviceRegistryService = new DeviceRegistryServiceImpl();
    DeviceFactory deviceFactory = new DeviceFactoryImpl();
    DeviceCreator deviceCreator = null;
    DeviceImpl device = null;
    DeviceQuery query = null;

    // Check if exception was fired in step.
    boolean exceptionCaught = false;

    // *************************************
    // Definition of Cucumber scenario steps
    // *************************************

    // Setup and tear-down steps

    @Before
    public void beforeScenario(Scenario scenario) throws Exception {
        container.startup();
        locator = KapuaLocatorImpl.getInstance();

        this.scenario = scenario;
        exceptionCaught = false;

        // Set up the mock locator
        MockedLocator mockLocator = (MockedLocator) locator;

        // Inject mocked Authorization Service method checkPermission
        AuthorizationService mockedAuthorization = mock(AuthorizationService.class);
        Mockito.doNothing().when(mockedAuthorization).checkPermission(any(Permission.class));
        mockLocator.setMockedService(AuthorizationService.class, mockedAuthorization);

        // Inject mocked Permission Factory
        PermissionFactory mockedPermissionFactory = mock(PermissionFactory.class);
        mockLocator.setMockedFactory(PermissionFactory.class, mockedPermissionFactory);

        // Inject commons objects
        mockLocator.setMockedFactory(KapuaIdFactory.class, kapuaIdFactory);

        // Inject the validation object
        mockLocator.setMockedService(DeviceRegistryService.class, deviceRegistryService);
        mockLocator.setMockedFactory(DeviceFactory.class, deviceFactory);
        // deviceValidator = new DeviceValidation();

        // Set KapuaMetatypeFactory for Metatype configuration
        mockLocator.setMockedFactory(KapuaMetatypeFactory.class, new KapuaMetatypeFactoryImpl());

        // All operations on database are performed using system user.
        KapuaSession kapuaSession = new KapuaSession(null, new KapuaEid(BigInteger.ONE), new KapuaEid(BigInteger.ONE));
        KapuaSecurityUtils.setSession(kapuaSession);
    }

    @After
    public void afterScenario()
            throws Exception {
        KapuaSecurityUtils.clearSession();

        container.shutdown();
    }

    // The Cucumber test steps

    @Given("^A regular device creator$")
    public void createARegularDeviceCreator()
            throws KapuaException {
        deviceCreator = prepareRegularDeviceCreator(rootScopeId, "testDev");
    }

    @Given("^A null device creator$")
    public void createANullDeviceCreator()
            throws KapuaException {
        deviceCreator = null;
    }

    @Given("^A regular device$")
    public void createRegularDevice()
            throws KapuaException {
        device = prepareRegularDevice(rootScopeId, new KapuaEid(BigInteger.valueOf(random.nextLong())));
    }

    @Given("^A null device$")
    public void createANullDevice()
            throws KapuaException {
        device = null;
    }

    @Given("^A regular query$")
    public void createRegularQuery() {
        query = deviceFactory.newQuery(rootScopeId);
    }

    @Given("^A query with a null Scope ID$")
    public void createQueryWithNullScopeId() {
        query = deviceFactory.newQuery(null);
    }

    @Given("^A null query$")
    public void createNullQuery() {
        query = null;
    }

    @When("^I set the creator scope ID to null$")
    public void setDeviceCreatorScopeToNull() {
        deviceCreator.setScopeId(null);
    }

    @When("^I set the creator client ID to null$")
    public void setDeviceCreatorClientToNull() {
        deviceCreator.setClientId(null);
    }

    @When("^I set the device scope ID to null$")
    public void setDeviceScopeToNull() {
        device.setScopeId(null);
    }

    @When("^I set the device ID to null$")
    public void setDeviceIdToNull() {
        device.setId(null);
    }

    @When("^I validate the device creator$")
    public void validateExistingDeviceCreator()
            throws KapuaException {
        try {
            exceptionCaught = false;
            DeviceValidation.validateCreatePreconditions(deviceCreator);
        } catch (KapuaException ex) {
            exceptionCaught = true;
        }
    }

    @When("^I validate the device for updates$")
    public void validateExistingDeviceForUpdates()
            throws KapuaException {
        try {
            exceptionCaught = false;
            DeviceValidation.validateUpdatePreconditions(device);
        } catch (KapuaException ex) {
            exceptionCaught = true;
        }
    }

    @When("^Validating a find operation for scope (.+) and device (.+)$")
    public void validateDeviceSearch(String scopeId, String deviceId)
            throws KapuaException {
        KapuaId scope;
        KapuaId dev;

        if (scopeId.trim().toLowerCase().equals("null")) {
            scope = null;
        } else {
            scope = new KapuaEid(new BigInteger(scopeId));
        }

        if (deviceId.trim().toLowerCase().equals("null")) {
            dev = null;
        } else {
            dev = new KapuaEid(new BigInteger(deviceId));
        }

        try {
            exceptionCaught = false;
            DeviceValidation.validateFindPreconditions(scope, dev);
        } catch (KapuaException ex) {
            exceptionCaught = true;
        }
    }

    @When("^Validating a find operation for scope (.+) and client \"(.*)\"$")
    public void validateDeviceSearchByClientId(String scopeId, String clientId)
            throws KapuaException {
        KapuaId scope;
        String client;

        if (scopeId.trim().toLowerCase().equals("null")) {
            scope = null;
        } else {
            scope = new KapuaEid(new BigInteger(scopeId));
        }

        if (clientId.trim().toLowerCase().equals("null")) {
            client = null;
        } else {
            client = clientId;
        }

        try {
            exceptionCaught = false;
            DeviceValidation.validateFindByClientIdPreconditions(scope, client);
        } catch (KapuaException ex) {
            exceptionCaught = true;
        }
    }

    @When("^Validating a delete operation for scope (.+) and device (.+)$")
    public void validateDeviceDelete(String scopeId, String deviceId)
            throws KapuaException {
        KapuaId scope;
        KapuaId dev;

        if (scopeId.trim().toLowerCase().equals("null")) {
            scope = null;
        } else {
            scope = new KapuaEid(new BigInteger(scopeId));
        }

        if (deviceId.trim().toLowerCase().equals("null")) {
            dev = null;
        } else {
            dev = new KapuaEid(new BigInteger(deviceId));
        }

        try {
            exceptionCaught = false;
            DeviceValidation.validateDeletePreconditions(scope, dev);
        } catch (KapuaException ex) {
            exceptionCaught = true;
        }
    }

    @When("^I validate a query operation$")
    public void checkQueryOperation()
            throws KapuaException {
        try {
            exceptionCaught = false;
            DeviceValidation.validateQueryPreconditions(query);
        } catch (KapuaException ex) {
            exceptionCaught = true;
        }
    }

    @When("^I validate a count operation$")
    public void checkCountOperation()
            throws KapuaException {
        try {
            exceptionCaught = false;
            DeviceValidation.validateCountPreconditions(query);
        } catch (KapuaException ex) {
            exceptionCaught = true;
        }
    }

    @Then("^No exception is thrown$")
    public void checkThatNoExceptionWasCaught() {
        assertFalse(exceptionCaught);
    }

    @Then("^An exception is thrown$")
    public void checkThatAnExceptionWasCaught() {
        assertTrue(exceptionCaught);
    }

    // *******************
    // * Private Helpers *
    // *******************

    // Create a device creator object. The creator is pre-filled with default data.
    private DeviceCreator prepareRegularDeviceCreator(KapuaId accountId, String client) {
        DeviceCreatorImpl tmpDeviceCreator = (DeviceCreatorImpl) deviceFactory.newCreator(accountId, client);

        tmpDeviceCreator.setClientId(client);
        tmpDeviceCreator.setConnectionId(new KapuaEid(BigInteger.valueOf(random.nextLong())));
        tmpDeviceCreator.setDisplayName("test_name");
        tmpDeviceCreator.setSerialNumber("serialNumber");
        tmpDeviceCreator.setModelId("modelId");
        tmpDeviceCreator.setImei(String.valueOf(random.nextInt()));
        tmpDeviceCreator.setImsi(String.valueOf(random.nextInt()));
        tmpDeviceCreator.setIccid(String.valueOf(random.nextInt()));
        tmpDeviceCreator.setBiosVersion("biosVersion");
        tmpDeviceCreator.setFirmwareVersion("firmwareVersion");
        tmpDeviceCreator.setOsVersion("osVersion");
        tmpDeviceCreator.setJvmVersion("jvmVersion");
        tmpDeviceCreator.setOsgiFrameworkVersion("osgiFrameworkVersion");
        tmpDeviceCreator.setApplicationFrameworkVersion("kapuaVersion");
        tmpDeviceCreator.setApplicationIdentifiers("applicationIdentifiers");
        tmpDeviceCreator.setAcceptEncoding("acceptEncoding");
        tmpDeviceCreator.setGpsLatitude(45.2);
        tmpDeviceCreator.setGpsLongitude(26.3);
        tmpDeviceCreator.setCustomAttribute1("customAttribute1");
        tmpDeviceCreator.setCustomAttribute2("customAttribute2");
        tmpDeviceCreator.setCustomAttribute3("customAttribute3");
        tmpDeviceCreator.setCustomAttribute4("customAttribute4");
        tmpDeviceCreator.setCustomAttribute5("customAttribute5");
        tmpDeviceCreator.setCredentialsMode(LOOSE);
        tmpDeviceCreator.setPreferredUserId(new KapuaEid(BigInteger.valueOf(random.nextLong())));
        tmpDeviceCreator.setStatus(DeviceStatus.ENABLED);

        return tmpDeviceCreator;
    }

    // Create a device object. The device is pre-filled with default data.
    private DeviceImpl prepareRegularDevice(KapuaId accountId, KapuaId deviceId) {
        DeviceImpl tmpDevice = (DeviceImpl) deviceFactory.newEntity(accountId);

        tmpDevice.setId(deviceId);
        tmpDevice.setConnectionId(new KapuaEid(BigInteger.valueOf(random.nextLong())));
        tmpDevice.setDisplayName("test_name");
        tmpDevice.setSerialNumber("serialNumber");
        tmpDevice.setModelId("modelId");
        tmpDevice.setImei(String.valueOf(random.nextInt()));
        tmpDevice.setImsi(String.valueOf(random.nextInt()));
        tmpDevice.setIccid(String.valueOf(random.nextInt()));
        tmpDevice.setBiosVersion("biosVersion");
        tmpDevice.setFirmwareVersion("firmwareVersion");
        tmpDevice.setOsVersion("osVersion");
        tmpDevice.setJvmVersion("jvmVersion");
        tmpDevice.setOsgiFrameworkVersion("osgiFrameworkVersion");
        tmpDevice.setApplicationFrameworkVersion("kapuaVersion");
        tmpDevice.setApplicationIdentifiers("applicationIdentifiers");
        tmpDevice.setAcceptEncoding("acceptEncoding");
        tmpDevice.setCustomAttribute1("customAttribute1");
        tmpDevice.setCustomAttribute2("customAttribute2");
        tmpDevice.setCustomAttribute3("customAttribute3");
        tmpDevice.setCustomAttribute4("customAttribute4");
        tmpDevice.setCustomAttribute5("customAttribute5");
        tmpDevice.setCredentialsMode(LOOSE);
        tmpDevice.setPreferredUserId(new KapuaEid(BigInteger.valueOf(random.nextLong())));
        tmpDevice.setStatus(DeviceStatus.ENABLED);

        return tmpDevice;
    }
}
