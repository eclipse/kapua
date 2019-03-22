/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.message.internal.device.lifecycle;

import org.eclipse.kapua.message.device.lifecycle.KapuaAppsPayload;

/**
 * {@link KapuaAppsPayload} implementation.
 *
 * @since 1.0.0
 */
public class KapuaAppsPayloadImpl extends KapuaBirthPayloadImpl implements KapuaAppsPayload {

    /**
     * Constructor.
     *
     * @since 1.1.0
     */
    public KapuaAppsPayloadImpl() {
        super();
    }

    /**
     * Constructor.
     *
     * @see KapuaBirthPayloadImpl
     */
    public KapuaAppsPayloadImpl(String uptime,
                                String displayName,
                                String modelName,
                                String modelId,
                                String partNumber,
                                String serialNumber,
                                String firmware,
                                String firmwareVersion,
                                String bios,
                                String biosVersion,
                                String os,
                                String osVersion,
                                String jvm,
                                String jvmVersion,
                                String jvmProfile,
                                String containerFramework,
                                String containerFrameworkVersion,
                                String applicationFramework,
                                String applicationFrameworkVersion,
                                String connectionInterface,
                                String connectionIp,
                                String acceptEncoding,
                                String applicationIdentifiers,
                                String availableProcessors,
                                String totalMemory,
                                String osArch,
                                String modemImei,
                                String modemImsi,
                                String modemIccid) {
        super(uptime,
                displayName,
                modelName,
                modelId,
                partNumber,
                serialNumber,
                firmware,
                firmwareVersion,
                bios,
                biosVersion,
                os,
                osVersion,
                jvm,
                jvmVersion,
                jvmProfile,
                containerFramework,
                containerFrameworkVersion,
                applicationFramework,
                applicationFrameworkVersion,
                connectionInterface,
                connectionIp,
                acceptEncoding,
                applicationIdentifiers,
                availableProcessors,
                totalMemory,
                osArch,
                modemImei,
                modemImsi,
                modemIccid);
    }
}
