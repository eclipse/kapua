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
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.shared.model.device.management.bundles;

import org.eclipse.kapua.app.console.module.api.client.messages.ValidationMessages;
import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

import com.google.gwt.core.client.GWT;

public class GwtBundle extends KapuaBaseModel {

    private static final long serialVersionUID = -5494700079001271509L;

    @Override
    @SuppressWarnings("unchecked")
    public <X> X get(String property) {
        if ("statusLoc".equals(property)) {
            ValidationMessages msgs = GWT.create(ValidationMessages.class);
            return (X) msgs.getString(getStatus());
        }
        return super.get(property);
    }

    public void setId(String id) {
        set("id", id);
    }

    public String getId() {
        return get("id");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getName() {
        return get("name");
    }

    public void setStatus(String status) {
        set("status", status);
    }

    public String getStatus() {
        return get("status");
    }

    public void setVersion(String version) {
        set("version", version);
    }

    public String getVersion() {
        return get("version");
    }
}
