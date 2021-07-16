/*******************************************************************************
 * Copyright (c) 2020, 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.qa.integration.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;

public class DockerUtil {

    private static final Logger logger = LoggerFactory.getLogger(DockerUtil.class);

    private static DockerClient dockerClient;

    private DockerUtil() {
    }

    public static DockerClient getDockerClient() {
        if (dockerClient==null) {
            synchronized (DockerUtil.class) {
                if (dockerClient==null) {
                    logger.info("Creating docker client...");
                    dockerClient = new DefaultDockerClient("unix:///var/run/docker.sock");
                    logger.info("Creating docker client... DONE");
                }
            }
        }
        return dockerClient;
    }

}
