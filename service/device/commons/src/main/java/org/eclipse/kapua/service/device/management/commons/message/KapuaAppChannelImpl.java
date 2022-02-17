/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.commons.message;

import org.eclipse.kapua.service.device.management.message.KapuaAppChannel;
import org.eclipse.kapua.service.device.management.message.KapuaAppProperties;

import java.util.List;

/**
 * {@link KapuaAppChannel} implementation.
 *
 * @since 1.0.0
 */
public abstract class KapuaAppChannelImpl implements KapuaAppChannel {

    private static final long serialVersionUID = 3755325191379812125L;

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
