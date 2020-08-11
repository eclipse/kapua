/*******************************************************************************
 * Copyright (c) 2018, 2020 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpoint;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpointCreator;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpointQuery;

import java.util.List;

@RemoteServiceRelativePath("endpoint")
public interface GwtEndpointService extends RemoteService {

    public GwtEndpoint create(GwtEndpointCreator gwtEndpointCreator) throws GwtKapuaException;

    public GwtEndpoint update(GwtEndpoint gwtEndpoint) throws GwtKapuaException;

    public GwtEndpoint find(String scopeShortId, String roleShortId) throws GwtKapuaException;

    public PagingLoadResult<GwtEndpoint> query(PagingLoadConfig loadConfig, GwtEndpointQuery gwtEndpointQuery) throws GwtKapuaException;

    public void delete(String scopeId, String endpointId) throws GwtKapuaException;

    public ListLoadResult<GwtGroupedNVPair> getEndpointDescription(String scopeShortId, String endpointShortId) throws GwtKapuaException;

    public List<GwtEndpoint> findAll(String scopeId) throws GwtKapuaException;

}
