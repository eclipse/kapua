/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.qa.integration.steps;

/**
 * Data object used in Gherkin to create messaging broker.
 */
public class BrokerConfigData {

    private String name;

    private String brokerAddress;

    private String brokerIp;

    private String clusterName;

    private int mqttPort;

    private int mqttHostPort;

    private int mqttsPort;

    private int mqttsHostPort;

    private int webPort;

    private int webHostPort;

    private int debugPort;

    private int debugHostPort;

    private int brokerInternalDebugPort;

    private String dockerImage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrokerAddress() {
        return brokerAddress;
    }

    public void setBrokerAddress(String brokerAddress) {
        this.brokerAddress = brokerAddress;
    }

    public String getBrokerIp() {
        return brokerIp;
    }

    public void setBrokerIp(String brokerIp) {
        this.brokerIp = brokerIp;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public int getMqttPort() {
        return mqttPort;
    }

    public void setMqttPort(int mqttPort) {
        this.mqttPort = mqttPort;
    }

    public int getMqttHostPort() {
        return mqttHostPort;
    }

    public void setMqttHostPort(int mqttHostPort) {
        this.mqttHostPort = mqttHostPort;
    }

    public int getMqttsPort() {
        return mqttsPort;
    }

    public void setMqttsPort(int mqttsPort) {
        this.mqttsPort = mqttsPort;
    }

    public int getMqttsHostPort() {
        return mqttsHostPort;
    }

    public void setMqttsHostPort(int mqttsHostPort) {
        this.mqttsHostPort = mqttsHostPort;
    }

    public int getWebPort() {
        return webPort;
    }

    public void setWebPort(int webPort) {
        this.webPort = webPort;
    }

    public int getWebHostPort() {
        return webHostPort;
    }

    public void setWebHostPort(int webHostPort) {
        this.webHostPort = webHostPort;
    }

    public int getDebugPort() {
        return debugPort;
    }

    public void setDebugPort(int debugPort) {
        this.debugPort = debugPort;
    }

    public int getDebugHostPort() {
        return debugHostPort;
    }

    public void setDebugHostPort(int debugHostPort) {
        this.debugHostPort = debugHostPort;
    }

    public int getBrokerInternalDebugPort() {
        return brokerInternalDebugPort;
    }

    public void setBrokerInternalDebugPort(int brokerInternalDebugPort) {
        this.brokerInternalDebugPort = brokerInternalDebugPort;
    }

    public String getDockerImage() {
        return dockerImage;
    }

    public void setDockerImage(String dockerImage) {
        this.dockerImage = dockerImage;
    }
}
