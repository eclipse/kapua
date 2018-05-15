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
package org.eclipse.kapua.app.console.module.tag.shared.service;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTag;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTagCreator;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTagQuery;

import java.util.List;

@RemoteServiceRelativePath("tag")
public interface GwtTagService extends RemoteService {

    GwtTag create(GwtTagCreator gwtTagCreator) throws GwtKapuaException;

    GwtTag update(GwtTag gwtTag) throws GwtKapuaException;

    GwtTag find(String scopeShortId, String roleShortId) throws GwtKapuaException;

    PagingLoadResult<GwtTag> query(PagingLoadConfig loadConfig, GwtTagQuery gwtTagQuery) throws GwtKapuaException;

    void delete(String scopeId, String tagId) throws GwtKapuaException;

    ListLoadResult<GwtGroupedNVPair> getTagDescription(String scopeShortId, String tagShortId) throws GwtKapuaException;

    List<GwtTag> findAll(String scopeId) throws GwtKapuaException;

    PagingLoadResult<GwtTag> findByDeviceId(PagingLoadConfig loadConfig, String gwtScopeId, String gwtDeviceId) throws GwtKapuaException;

}
