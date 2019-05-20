/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.proxy.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

public enum JobEngineHttpProxySettingKey implements SettingKey {

    /**
     * HTTP base address for the Job Engine Microservice
     */
    MICROSERVICE_JOBENGINE_HTTP_BASEADDRESS("microservice.jobengine.http.baseaddress");

    private String key;

    private JobEngineHttpProxySettingKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
