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
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.shared.service;

import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessInfo;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessInfoCreator;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("accessinfo")
public interface GwtAccessInfoService extends RemoteService {

    public GwtAccessInfo create(GwtXSRFToken gwtXsrfToken, GwtAccessInfoCreator gwtAccessInfoCreator)
            throws GwtKapuaException;
    
    public void delete(GwtXSRFToken gwtXsrfToken, String scopeShortId, String roleShortId)
            throws GwtKapuaException;
    
    public GwtAccessInfo findByUserIdOrCreate(String scopeShortId, String userShortId)
            throws GwtKapuaException;
}