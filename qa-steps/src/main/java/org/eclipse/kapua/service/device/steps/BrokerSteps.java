/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.steps;

import com.google.inject.Inject;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.setting.BrokerSetting;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.qa.steps.EmbeddedBroker;
import org.eclipse.kapua.service.StepData;
import org.eclipse.kapua.service.TestJAXBContextProvider;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundle;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundleManagementService;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundles;
import org.eclipse.kapua.service.device.management.command.DeviceCommandFactory;
import org.eclipse.kapua.service.device.management.command.DeviceCommandInput;
import org.eclipse.kapua.service.device.management.command.DeviceCommandManagementService;
import org.eclipse.kapua.service.device.management.command.DeviceCommandOutput;
import org.eclipse.kapua.service.device.management.configuration.DeviceComponentConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationManagementService;
import org.eclipse.kapua.service.device.management.packages.DevicePackageManagementService;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackage;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackages;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshotManagementService;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.Assert;

import java.math.BigInteger;
import java.util.List;

/**
 * Steps used in integration scenarios with running MQTT broker and process of
 * registering mocked Kura device registering with Kapua and issuing basic administrative
 * commands on Mocked Kura.
 */
@ScenarioScoped
public class BrokerSteps extends Assert {

    /**
     * Embedded broker configuration file from classpath resources.
     */
    public static final String ACTIVEMQ_XML = "xbean:activemq.xml";

    /**
     * Device birth topic.
     */
    private static final String MQTT_BIRTH = "$EDC/kapua-sys/rpione3/MQTT/BIRTH";

    /**
     * Device death topic.
     */
    private static final String MQTT_DC = "$EDC/kapua-sys/rpione3/MQTT/DC";

    /**
     * URI of mqtt broker.
     */
    private static final String BROKER_URI = "tcp://localhost:1883";

    /**
     * Access to device management service.
     */
    private DevicePackageManagementService devicePackageManagementService;

    /**
     * Device registry service for managing devices in DB.
     */
    private DeviceRegistryService deviceRegistryService;

    /**
     * Configuration service for kura devices.
     */
    private DeviceConfigurationManagementService deviceConfiguratiomManagementService;

    /**
     * Kura snapshot management service.
     */
    @SuppressWarnings("unused")
    private DeviceSnapshotManagementService deviceSnapshotManagementService;

    /**
     * Kura bundle management service.
     */
    private DeviceBundleManagementService deviceBundleManagementService;

    /**
     * Service for issuing commnads on Kura device.
     */
    private DeviceCommandManagementService deviceCommandManagementService;

    /**
     * Factory for creating commands sent to Kura.
     */
    private DeviceCommandFactory deviceCommandFactory;

    /**
     * Service for connecting devices.
     */
    private static DeviceConnectionService deviceConnectionService;

    /**
     * Client simulating Kura device
     */
    private KuraDevice kuraDevice;

    /**
     * Scenario scoped step data.
     */
    private StepData stepData;

    @Inject
    public BrokerSteps(/* dependency */ EmbeddedBroker broker, StepData stepData) {
        this.stepData = stepData;
    }

    @Before
    public void beforeScenario(Scenario scenario) throws Exception {

        BrokerSetting.resetInstance();

        KapuaLocator locator = KapuaLocator.getInstance();
        devicePackageManagementService = locator.getService(DevicePackageManagementService.class);
        deviceRegistryService = locator.getService(DeviceRegistryService.class);
        deviceConfiguratiomManagementService = locator.getService(DeviceConfigurationManagementService.class);
        deviceSnapshotManagementService = locator.getService(DeviceSnapshotManagementService.class);
        deviceBundleManagementService = locator.getService(DeviceBundleManagementService.class);
        deviceCommandManagementService = locator.getService(DeviceCommandManagementService.class);
        deviceCommandFactory = locator.getFactory(DeviceCommandFactory.class);
        deviceConnectionService = locator.getService(DeviceConnectionService.class);

        JAXBContextProvider consoleProvider = new TestJAXBContextProvider();
        XmlUtil.setContextProvider(consoleProvider);
    }

    @After
    public void afterScenario() throws Exception {

        if (kuraDevice != null) {
            this.kuraDevice.mqttClientDisconnect();
        }

        KapuaSecurityUtils.clearSession();

        this.stepData = null;
    }

    @When("^I start the Kura Mock$")
    public void startKuraMock() {

        kuraDevice = new KuraDevice();
        kuraDevice.mqttClientConnect();
    }

    @When("^Device birth message is sent$")
    public void deviceBirthMessage() throws Exception {

        kuraDevice.sendMessageFromFile(MQTT_BIRTH, 0, false, "src/test/resources/mqtt/rpione3_MQTT_BIRTH.mqtt");
    }

    @When("^Device is connected$")
    public void deviceConnected() throws Exception {

        deviceBirthMessage();
    }

    @When("^Device death message is sent$")
    public void deviceDeathMessage() throws Exception {

        kuraDevice.sendMessageFromFile(MQTT_DC, 0, false, "src/test/resources/mqtt/rpione3_MQTT_DC.mqtt");
    }

    @When("^Device is disconnected$")
    public void deviceDisconnected() throws Exception {

        deviceDeathMessage();
    }

    @When("^Packages are requested$")
    public void requestPackages() throws Exception {

        Device device = deviceRegistryService.findByClientId(new KapuaEid(BigInteger.valueOf(1l)), "rpione3");
        if (device != null) {
            DevicePackages deploymentPackages = devicePackageManagementService.getInstalled(device.getScopeId(),
                    device.getId(), null);
            List<DevicePackage> packages = deploymentPackages.getPackages();
            stepData.put("packages", packages);
        }
    }

    @Then("^Packages are received$")
    public void packagesReceived() {

        @SuppressWarnings("unchecked")
        List<DevicePackage> packages = (List<DevicePackage>) stepData.get("packages");
        if (packages != null) {
            assertEquals(1, packages.size());
        }
    }

    @When("^Bundles are requested$")
    public void requestBundles() throws Exception {

        Device device = deviceRegistryService.findByClientId(new KapuaEid(BigInteger.valueOf(1l)), "rpione3");
        Assert.assertNotNull(device);
        DeviceBundles deviceBundles = deviceBundleManagementService.get(device.getScopeId(), device.getId(), null);
        List<DeviceBundle> bundles = deviceBundles.getBundles();
        stepData.put("bundles", bundles);
    }

    @Then("^Bundles are received$")
    public void bundlesReceived() {
        @SuppressWarnings("unchecked")
        List<DeviceBundle> bundles = (List<DeviceBundle>) stepData.get("bundles");
        assertEquals(80, bundles.size());
    }

    @When("^Configuration is requested$")
    public void requestConfiguration() throws Exception {

        Device device = deviceRegistryService.findByClientId(new KapuaEid(BigInteger.valueOf(1l)), "rpione3");
        DeviceConfiguration deviceConfiguration = deviceConfiguratiomManagementService.get(device.getScopeId(),
                device.getId(), null, null, null);
        List<DeviceComponentConfiguration> configurations = deviceConfiguration.getComponentConfigurations();
        stepData.put("configurations", configurations);
    }

    @Then("^Configuration is received$")
    public void configurationReceived() {
        @SuppressWarnings("unchecked")
        List<DeviceComponentConfiguration> configurations = (List<DeviceComponentConfiguration>) stepData.get("configurations");
        assertEquals(11, configurations.size());
    }

    @When("^Command (.*) is executed$")
    public void executeCommand(String command) throws Exception {

        Device device = deviceRegistryService.findByClientId(new KapuaEid(BigInteger.valueOf(1l)), "rpione3");
        DeviceCommandInput commandInput = deviceCommandFactory.newCommandInput();
        commandInput.setCommand(command);
        commandInput.setRunAsynch(false);
        commandInput.setTimeout(0);
        DeviceCommandOutput deviceCommandOutput = deviceCommandManagementService.exec(device.getScopeId(),
                device.getId(), commandInput, null);
        Integer commandExitCode = deviceCommandOutput.getExitCode();
        stepData.put("commandExitCode", commandExitCode);
    }

    @Then("^Exit code (\\d+) is received$")
    public void configurationReceived(int expectedExitCode) {

        Integer commandExitCode = (Integer) stepData.get("commandExitCode");
        assertEquals(expectedExitCode, commandExitCode.intValue());
    }

    @Then("^Device is connected with \"(.*)\" server ip$")
    public void deviceWithServerIp(String serverIp) {

        DeviceConnection deviceConn = null;
        stepData.put("ExceptionCaught", false);
        try {
            deviceConn = deviceConnectionService.findByClientId(new KapuaEid(BigInteger.valueOf(1l)), "rpione3");
        } catch (KapuaException ex) {
            stepData.put("ExceptionCaught", true);
            return;
        }
        assertEquals(serverIp, deviceConn.getServerIp());
    }


    @When("^Client with name \"(.*)\" with client id \"(.*)\" user \"(.*)\" password \"(.*)\" is connected$")
    public void clientConnect(String clientName, String clientId, String user, String password) throws Exception {
        MqttClient mqttClient = null;
        MqttConnectOptions clientOpts = new MqttConnectOptions();

        try {
            mqttClient = new MqttClient(BROKER_URI, clientId,
                    new MemoryPersistence());
        } catch (MqttException e) {
            e.printStackTrace();
        }
        clientOpts.setUserName(user);
        clientOpts.setPassword(password.toCharArray());
        try {
            mqttClient.connect(clientOpts);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        if (mqttClient != null) {
            stepData.put(clientName, mqttClient);
        } else {
            throw new Exception("Mqtt test client not connected.");
        }
    }

    @When("^topic \"(.*)\" content \"(.*)\" is published by client named \"(.*)\"$")
    public void publishMessageByClient(String topic, String content, String clientName) throws Exception {
        MqttClient mqttClient = (MqttClient) stepData.get(clientName);
        if (mqttClient == null) {
            throw new Exception("Mqtt test client not found");
        }
        mqttClient.publish(topic, content.getBytes(), 0, false);
    }

    @Then("^Client named \"(.*)\" is connected$")
    public void clientConnected(String clientName) throws Exception {
        MqttClient mqttClient = (MqttClient) stepData.get(clientName);
        if (mqttClient == null) {
            throw new Exception("Mqtt test client not found");
        }
        assertEquals(true, mqttClient.isConnected());
    }

    @Then("^Client named \"(.*)\" is not connected$")
    public void clientNotConnected(String clientName) throws Exception {
        MqttClient mqttClient = (MqttClient) stepData.get(clientName);
        if (mqttClient == null) {
            throw new Exception("Mqtt test client not found");
        }
        assertEquals(false, mqttClient.isConnected());
    }

    @Then("^Disconnect client with name \"(.*)\"$")
    public void disconnectClient(String clientName) throws Exception {
        MqttClient mqttClient = (MqttClient) stepData.get(clientName);
        if (mqttClient == null) {
            throw new Exception("Mqtt test client not found");
        }
        try {
            mqttClient.disconnect();
            mqttClient.close();
        } catch (Exception e) {
            // Exception eaten on purpose.
            // Disconnect is for sanity only.
        }
    }

}
