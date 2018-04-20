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
package org.eclipse.kapua.service.device.call.kura.model.deploy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Kura deployment packages.
 * 
 * @since 1.0
 *
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
