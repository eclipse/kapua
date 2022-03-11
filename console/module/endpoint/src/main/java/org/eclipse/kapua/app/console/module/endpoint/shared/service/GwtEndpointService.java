/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.endpoint.shared.service;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.endpoint.client.EndpointModel;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpoint;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpointCreator;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpointQuery;

import java.util.List;

@RemoteServiceRelativePath("endpoint")
public interface GwtEndpointService extends RemoteService {

    GwtEndpoint create(GwtXSRFToken gwtXsrfToken, GwtEndpointCreator gwtEndpointCreator) throws GwtKapuaException;

    GwtEndpoint update(GwtXSRFToken gwtXsrfToken, GwtEndpoint gwtEndpoint) throws GwtKapuaException;

    GwtEndpoint find(String scopeShortId, String roleShortId) throws GwtKapuaException;

    PagingLoadResult<GwtEndpoint> query(PagingLoadConfig loadConfig, GwtEndpointQuery gwtEndpointQuery, String section) throws GwtKapuaException;

    void delete(GwtXSRFToken gwtXsrfToken, String scopeId, String endpointId) throws GwtKapuaException;

    ListLoadResult<GwtGroupedNVPair> getEndpointDescription(String scopeShortId, String endpointShortId) throws GwtKapuaException;

    List<GwtEndpoint> findAll(String scopeId) throws GwtKapuaException;

    public EndpointModel parseEndpointModel(EndpointModel endpointModel, String origin) throws GwtKapuaException;

}
