/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.tag.shared.model;

import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;

import java.util.ArrayList;
import java.util.List;

public class GwtTagQuery extends GwtQuery {

    private static final long serialVersionUID = -4379272962842143514L;

    private String name;
    private String description;
    private List<String> ids;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
