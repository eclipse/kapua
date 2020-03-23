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
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Kura deployment package.
 *
 * @since 1.0
 *
 */
@XmlRootElement(name = "package")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType
public class KuraDeploymentPackage {

    public String name;
    public String version;
    public KuraBundleInfo[] bundleInfos;

    /**
     * Get the deployment package name
     *
     * @return
     */
    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    /**
     * Set the deployment package name
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the deployment package version
     *
     * @return
     */
    @XmlElement(name = "version")
    public String getVersion() {
        return version;
    }

    /**
     * Set the deployment package version
     *
     * @param version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Get the bundle information array
     *
     * @return
     */
    @XmlElementWrapper(name = "bundles")
    @XmlElement(name = "bundle")
    public KuraBundleInfo[] getBundleInfos() {
        return bundleInfos;
    }

    /**
     * Set the bundle information array
     *
     * @param bundleInfos
     */
    public void setBundleInfos(KuraBundleInfo[] bundleInfos) {
        this.bundleInfos = bundleInfos;
    }
}
