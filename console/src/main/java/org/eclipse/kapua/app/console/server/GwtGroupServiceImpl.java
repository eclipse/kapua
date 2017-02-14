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
package org.eclipse.kapua.app.console.server;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtGroup;
import org.eclipse.kapua.app.console.shared.service.GwtGroupService;
import org.eclipse.kapua.app.console.shared.util.GwtKapuaModelConverter;
import org.eclipse.kapua.app.console.shared.util.KapuaGwtModelConverter;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.authorization.group.*;

import java.util.ArrayList;
import java.util.List;

public class GwtGroupServiceImpl extends KapuaRemoteServiceServlet implements GwtGroupService {

    @Override
    public List<GwtGroup> findAll(String scopeId) {
        List<GwtGroup> groupList = new ArrayList<>();
        KapuaLocator locator = KapuaLocator.getInstance();
        GroupService groupService = locator.getService(GroupService.class);
        GroupFactory groupFactory = locator.getFactory(GroupFactory.class);
        GroupQuery query = groupFactory.newQuery(GwtKapuaModelConverter.convert(scopeId));
        try {
            GroupListResult result = groupService.query(query);
            for (Group group : result.getItems()) {
                groupList.add(KapuaGwtModelConverter.convert(group));
            }
        } catch (KapuaException e) {
            e.printStackTrace();
        }
        return groupList;
    }
}
