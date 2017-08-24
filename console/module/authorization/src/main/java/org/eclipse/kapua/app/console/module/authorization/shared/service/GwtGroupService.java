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
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.authorization.shared.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtGroup;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtGroupedNVPair;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtGroupCreator;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtGroupQuery;

@RemoteServiceRelativePath("group")
public interface GwtGroupService extends RemoteService {

    public GwtGroup create(GwtGroupCreator gwtGroupCreator) throws GwtKapuaException;

    public GwtGroup update(GwtGroup gwtGroup) throws GwtKapuaException;

    public GwtGroup find(String scopeShortId, String roleShortId) throws GwtKapuaException;

    public PagingLoadResult<GwtGroup> query(PagingLoadConfig loadConfig,
            GwtGroupQuery gwtGroupQuery) throws GwtKapuaException;

    public void delete(String scopeId, String groupId) throws GwtKapuaException;

    public ListLoadResult<GwtGroupedNVPair> getGroupDescription(String scopeShortId,
            String groupShortId) throws GwtKapuaException;

    public List<GwtGroup> findAll(String scopeId);

}
