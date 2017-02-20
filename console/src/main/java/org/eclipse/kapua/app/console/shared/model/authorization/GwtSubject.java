/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.shared.model.authorization;

import org.eclipse.kapua.app.console.shared.model.KapuaBaseModel;

public class GwtSubject extends KapuaBaseModel {

    private static final long serialVersionUID = -6427095082548965937L;

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X get(String property) {
        if ("subejctTypeEnum".equals(property)) {
            String subjecyType = getSubjectType();
            if (subjecyType != null)
                return (X) (GwtSubjectType.valueOf(subjecyType));
            return (X) "";
        } else {
            return super.get(property);
        }
    }

    public String getSubjectType() {
        return get("subejctType");
    }

    public GwtSubjectType getSubjectTypeEnum() {
        return get("subejctTypeEnum");
    }

    public void setSubjectType(String subejctType) {
        set("subejctType", subejctType);
    }

    public String getSubjectId() {
        return get("subejctId");
    }

    public void setSubjectId(String subejctId) {
        set("subejctId", subejctId);
    }
}
