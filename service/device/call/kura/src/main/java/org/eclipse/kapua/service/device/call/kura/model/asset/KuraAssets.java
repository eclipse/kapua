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
package org.eclipse.kapua.service.device.call.kura.model.asset;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Kura assets list definition.
 * 
 * @since 1.0
 *
 */
@XmlRootElement(name="assets")
@XmlAccessorType(XmlAccessType.FIELD)
public class KuraAssets
{
    @XmlElement(name="asset")
    public KuraAsset[] assets;

    /**
     * Get the assets list
     * 
     * @return
     */
    public KuraAsset[] getAssets()
    {
        return assets;
    }

    /**
     * Set the assets list
     * 
     * @param assets
     */
    public void setAssets(KuraAsset[] assets)
    {
        this.assets = assets;
    }
}
