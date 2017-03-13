/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.kapua.qa.openshift;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class OpenShiftTest {

    @Test
    public void shouldSuccessfullyDeployBroker() throws IOException {
        String oc = "/tmp/openshift/openshift-origin-server-v1.4.1+3f9807a-linux-64bit/oc";
        Process process = new ProcessBuilder().command(oc, "get", "pod").redirectErrorStream(true).start();
        List<String> output = Arrays.asList(IOUtils.toString(process.getInputStream()).split("\\n"));
        String brokerPod = output.stream().filter(line -> line.startsWith("kapua-broker-")).collect(Collectors.toList()).get(0);
        assertThat(brokerPod).contains("Running");
    }

    @Test
    public void shouldSuccessfullyDeployApi() throws IOException {
        String oc = "/tmp/openshift/openshift-origin-server-v1.4.1+3f9807a-linux-64bit/oc";
        Process process = new ProcessBuilder().command(oc, "get", "pod").redirectErrorStream(true).start();
        List<String> output = Arrays.asList(IOUtils.toString(process.getInputStream()).split("\\n"));
        String brokerPod = output.stream().filter(line -> line.startsWith("kapua-api-")).collect(Collectors.toList()).get(0);
        assertThat(brokerPod).contains("Running");
    }

    @Test
    public void shouldSuccessfullyDeployConsole() throws IOException {
        String oc = "/tmp/openshift/openshift-origin-server-v1.4.1+3f9807a-linux-64bit/oc";
        Process process = new ProcessBuilder().command(oc, "get", "pod").redirectErrorStream(true).start();
        List<String> output = Arrays.asList(IOUtils.toString(process.getInputStream()).split("\\n"));
        String brokerPod = output.stream().filter(line -> line.startsWith("kapua-console-")).collect(Collectors.toList()).get(0);
        assertThat(brokerPod).contains("Running");
    }

    @Test
    public void shouldSuccessfullyDeploySqlServer() throws IOException {
        String oc = "/tmp/openshift/openshift-origin-server-v1.4.1+3f9807a-linux-64bit/oc";
        Process process = new ProcessBuilder().command(oc, "get", "pod").redirectErrorStream(true).start();
        List<String> output = Arrays.asList(IOUtils.toString(process.getInputStream()).split("\\n"));
        String brokerPod = output.stream().filter(line -> line.startsWith("sql-")).collect(Collectors.toList()).get(0);
        assertThat(brokerPod).contains("Running");
    }

    @Test
    public void shouldSuccessfullyDeployElasticSearch() throws IOException {
        String oc = "/tmp/openshift/openshift-origin-server-v1.4.1+3f9807a-linux-64bit/oc";
        Process process = new ProcessBuilder().command(oc, "get", "pod").redirectErrorStream(true).start();
        List<String> output = Arrays.asList(IOUtils.toString(process.getInputStream()).split("\\n"));
        String brokerPod = output.stream().filter(line -> line.startsWith("elasticsearch-")).collect(Collectors.toList()).get(0);
        assertThat(brokerPod).contains("Running");
    }

}
