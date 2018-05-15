/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
