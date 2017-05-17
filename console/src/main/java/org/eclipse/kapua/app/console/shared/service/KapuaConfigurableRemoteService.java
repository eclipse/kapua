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
package org.eclipse.kapua.app.console.shared.service;

import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtConfigComponent;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;

import com.google.gwt.user.client.rpc.RemoteService;

public interface KapuaConfigurableRemoteService extends RemoteService {

    /**
     * Updates the configuration of the provided component.
     *
     * @param scopeId
     * @param parentScopeId
     * @param configComponent
     */
    public void updateComponentConfiguration(GwtXSRFToken xsrfToken, String scopeId, String parentScopeId, GwtConfigComponent configComponent)
            throws GwtKapuaException;

}
