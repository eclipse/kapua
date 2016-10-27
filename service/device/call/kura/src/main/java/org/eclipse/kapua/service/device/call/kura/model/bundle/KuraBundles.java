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
 *
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
@XmlRootElement(name="bundles")
@XmlAccessorType(XmlAccessType.FIELD)
public class KuraBundles
{
    @XmlElement(name="bundle")
    public KuraBundle[] bundles;

    /**
     * Get the bundles list
     * 
     * @return
     */
    public KuraBundle[] getBundles()
    {
        return bundles;
    }

    /**
     * Set the bundles list
     * 
     * @param bundles
     */
    public void setBundles(KuraBundle[] bundles)
    {
        this.bundles = bundles;
    }
}
