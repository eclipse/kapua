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

import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;

public class GwtTag extends GwtUpdatableEntityModel {

    private static final long serialVersionUID = 1379154649673867888L;

    private static final String DESCRIPTION = "description";

    public String getTagName() {
        return get("tagName");
    }

    public void setTagName(String name) {
        set("tagName", name);
        set("value", name);
    }

    public String getTagDescription() {
        return get(DESCRIPTION);
    }

    public String getUnescapedDescription() {
        return (String) getUnescaped(DESCRIPTION);
    }

    public void setTagDescription(String description) {
        set(DESCRIPTION, description);
        set("value", description);
    }

    @Override
    public String toString() {
        return getTagName();
    }
}
