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
package org.eclipse.kapua.app.console.module.authorization.shared.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessInfo;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessInfoCreator;

@RemoteServiceRelativePath("accessinfo")
public interface GwtAccessInfoService extends RemoteService {

    GwtAccessInfo create(GwtXSRFToken gwtXsrfToken, GwtAccessInfoCreator gwtAccessInfoCreator)
            throws GwtKapuaException;

    void delete(GwtXSRFToken gwtXsrfToken, String scopeShortId, String roleShortId)
            throws GwtKapuaException;

    GwtAccessInfo findByUserIdOrCreate(String scopeShortId, String userShortId)
            throws GwtKapuaException;

}
