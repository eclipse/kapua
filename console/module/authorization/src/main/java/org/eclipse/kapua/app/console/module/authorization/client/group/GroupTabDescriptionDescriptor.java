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
package org.eclipse.kapua.app.console.module.authorization.client.group;

import org.eclipse.kapua.app.console.commons.client.views.AbstractTabDescriptor;
import org.eclipse.kapua.app.console.commons.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtGroup;

public class GroupTabDescriptionDescriptor extends AbstractTabDescriptor<GwtGroup, GroupTabDescription, GroupView> {

    @Override
    public GroupTabDescription getTabViewInstance(GroupView view, GwtSession currentSession) {
        return new GroupTabDescription();
    }

    @Override
    public String getViewId() {
        return "group.description";
    }

    @Override
    public Integer getOrder() {
        return 100;
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return true;
    }
}
