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
package org.eclipse.kapua.service.device.call.kura.model.bundle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Kura bundles list definition.
 *
 * @since 1.0
 *
 */
@XmlRootElement(name = "bundles")
@XmlAccessorType(XmlAccessType.FIELD)
public class KuraBundles {

    @XmlElement(name = "bundle")
    public KuraBundle[] bundles;

    /**
     * Get the bundles list
     *
     * @return
     */
    public KuraBundle[] getBundles() {
        return bundles;
    }

    /**
     * Set the bundles list
     *
     * @param bundles
     */
    public void setBundles(KuraBundle[] bundles) {
        this.bundles = bundles;
    }
}
