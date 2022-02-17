/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.device.shared.model.management.bundles;

import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.messages.ValidationMessages;
import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

public class GwtBundle extends KapuaBaseModel {

    private static final long serialVersionUID = -5494700079001271509L;

    @Override
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
