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
package org.eclipse.kapua.app.console.module.tag.shared.model;

import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;

import java.util.ArrayList;
import java.util.List;

public class GwtTagQuery extends GwtQuery {

    private static final long serialVersionUID = -4379272962842143514L;

    private String name;
    private List<String> ids;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getIds() {
        if (ids == null) {
            ids = new ArrayList<String>();
        }

        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
