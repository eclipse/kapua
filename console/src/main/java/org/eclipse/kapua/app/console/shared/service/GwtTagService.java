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
package org.eclipse.kapua.app.console.shared.service;

import java.util.List;

import org.eclipse.kapua.app.console.client.tag.GwtTagCreator;
import org.eclipse.kapua.app.console.client.tag.GwtTagQuery;
import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.shared.model.GwtTag;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("tag")
public interface GwtTagService extends KapuaConfigurableRemoteService {

    public GwtTag create(GwtTagCreator gwtTagCreator) throws GwtKapuaException;

    public GwtTag update(GwtTag gwtTag) throws GwtKapuaException;

    public GwtTag find(String scopeShortId, String roleShortId) throws GwtKapuaException;

    public PagingLoadResult<GwtTag> query(PagingLoadConfig loadConfig,
            GwtTagQuery gwtTagQuery) throws GwtKapuaException;

    public void delete(String scopeId, String tagId) throws GwtKapuaException;

    public ListLoadResult<GwtGroupedNVPair> getTagDescription(String scopeShortId,
            String tagShortId) throws GwtKapuaException;

    public List<GwtTag> findAll(String scopeId);

}