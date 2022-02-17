/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.call.kura.model.deploy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Kura deployment packages.
 *
 * @since 1.0
 */
@XmlRootElement(name = "packages")
@XmlAccessorType(XmlAccessType.FIELD)
public class KuraDeploymentPackages {

    @XmlElement(name = "package")
    public KuraDeploymentPackage[] deploymentPackages;

    /**
     * Get the deployment package array
     *
     * @return
     */
    public KuraDeploymentPackage[] getDeploymentPackages() {
        if (deploymentPackages == null) {
            deploymentPackages = new KuraDeploymentPackage[0];
        }

        return deploymentPackages;
    }

    /**
     * Set the deployment package array
     *
     * @param deploymentPackages
     */
    public void setDeploymentPackages(KuraDeploymentPackage[] deploymentPackages) {
        this.deploymentPackages = deploymentPackages;
    }
}
