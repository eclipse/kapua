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
package org.eclipse.kapua.app.console.server;

import java.util.Map;

import org.eclipse.kapua.app.console.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtConfigComponent;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.util.GwtKapuaModelConverter;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

public abstract class KapuaConfigurableRemoteServiceServlet<S extends KapuaConfigurableService> extends KapuaRemoteServiceServlet {

    private static final long serialVersionUID = -2349296205840554149L;

    protected final S configurableService;

    protected KapuaConfigurableRemoteServiceServlet(S configurableService) {
        this.configurableService = configurableService;
    }

    public void updateComponentConfiguration(GwtXSRFToken xsrfToken, String scopeId, String parentId, GwtConfigComponent gwtConfigComponent) throws GwtKapuaException {
        //
        // Checking validity of the given XSRF Token
        checkXSRFToken(xsrfToken);
        KapuaId kapuaScopeId = GwtKapuaModelConverter.convert(scopeId);
        KapuaId kapuaParentId = GwtKapuaModelConverter.convert(parentId);
        try {
            // execute the update
            Map<String, Object> configParameters = GwtKapuaModelConverter.convert(gwtConfigComponent);

            configurableService.setConfigValues(kapuaScopeId, kapuaParentId, configParameters);

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }
}
