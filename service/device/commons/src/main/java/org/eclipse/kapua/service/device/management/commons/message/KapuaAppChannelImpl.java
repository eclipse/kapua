/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.commons.message;

import java.util.List;

import org.eclipse.kapua.service.device.management.KapuaAppChannel;
import org.eclipse.kapua.service.device.management.KapuaAppProperties;

/**
 * Kapua application message channel implementation.<br>
 * 
 * @since 1.0
 *
 */
public abstract class KapuaAppChannelImpl implements KapuaAppChannel {

    private List<String> semanticParts;
    private KapuaAppProperties appName;
    private KapuaAppProperties appVersion;

    @Override
    public List<String> getSemanticParts() {
        return semanticParts;
    }

    @Override
    public void setSemanticParts(List<String> semanticParts) {
        this.semanticParts = semanticParts;
    }

    @Override
    public KapuaAppProperties getAppName() {
        return appName;
    }

    @Override
    public void setAppName(KapuaAppProperties appName) { // do I have to keep it as a KapuaAppProperties?
        this.appName = appName;
    }

    @Override
    public KapuaAppProperties getVersion() {
        return appVersion;
    }

    @Override
    public void setVersion(KapuaAppProperties appVersion) {
        this.appVersion = appVersion;
    }

}
