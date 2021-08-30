/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine.app.web.jaxb;

import org.eclipse.kapua.app.web.commons.xml.JaxRSJAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Providers;

public class JobEngineJAXBContextProvider extends JaxRSJAXBContextProvider implements JAXBContextProvider {

    @Context
    Providers providers;

    @Override
    protected Providers getProviders() {
        return providers;
    }

    @Override
    protected MediaType getMediaType() {
        return MediaType.APPLICATION_JSON_TYPE;
    }
}
