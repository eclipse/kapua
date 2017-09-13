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
package org.eclipse.kapua.app.console.module.api.shared.model;

import java.io.Serializable;

import org.eclipse.kapua.app.console.module.api.client.util.KapuaSafeHtmlUtils;

import com.extjs.gxt.ui.client.data.BaseTreeModel;

public class KapuaBaseTreeModel extends BaseTreeModel implements Serializable {

    private static final long serialVersionUID = 167866061149596287L;

    public KapuaBaseTreeModel() {
        super();
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X set(String name, X value) {
        if (value instanceof String) {
            value = (X) KapuaSafeHtmlUtils.htmlEscape((String) value);
        }

        return super.set(name, value);
    }

    @SuppressWarnings({ "unchecked" })
    public <X> X getUnescaped(String property) {
        X value = super.get(property);

        if (value instanceof String) {
            value = (X) KapuaSafeHtmlUtils.htmlUnescape((String) value);
        }

        return value;
    }
}
