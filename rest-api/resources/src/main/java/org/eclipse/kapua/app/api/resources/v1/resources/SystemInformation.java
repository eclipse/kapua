/*******************************************************************************
 * Copyright (c) 2023, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.api.resources.v1.resources;

import org.eclipse.kapua.app.api.core.resources.AbstractKapuaResource;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.systeminfo.SystemInfo;
import org.eclipse.kapua.service.systeminfo.SystemInfoService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/sys-info")
public class SystemInformation extends AbstractKapuaResource {
    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final SystemInfoService systemInfoService = locator.getService(SystemInfoService.class);


    /**
     * Gets the system info.
     *
     * @since 2.0.0
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public SystemInfo getSystemInfo() {
        return systemInfoService.getSystemInfo();
    }

}